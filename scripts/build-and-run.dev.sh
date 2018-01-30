#!/usr/bin/env bash

set -e
set -x

# Parse options
ORIENTDB_HOST=${ORIENTDB_HOST:-localhost}
while [ "$#" != "0" ] ; do
    if   [ "$1" == "--orientdb-host" ] ; then
        ORIENTDB_HOST="$2"
        shift
    elif [ "$1" == "--start-orientdb-server" ] ; then
        START_ORIENTDB_SERVER=true
    elif [ "$1" == "--only-start-orientdb-server" ] ; then
        START_ORIENTDB_SERVER=true
        ONLY_START_ORIENTDB_SERVER=true
    else
        echo "Unknown parameter '$1'"
        exit 1
    fi
    shift
done

# Configure/reconfigure proxy in case it is set
if [ -f /opt/debattons/setup/proxy.sh ]; then
    /opt/debattons/setup/proxy.sh
fi

export ORIENTDB_ROOT_PASSWORD=${ORIENTDB_ROOT_PASSWORD:-default_ORIENTDB_ROOT_PASSWORD_to_be_changed}
ORIENTDB_PATH=${ORIENTDB_PATH:-/opt/orientdb}

if [ "$START_ORIENTDB_SERVER" = "true" ]; then
    $ORIENTDB_PATH/bin/server.sh &
fi

# Creating the api-server user if not exist
while ! nc -vz "$ORIENTDB_HOST" 2424; do
    sleep 1
done

# Also need to create the "Root" class due to the following issue https://github.com/orientechnologies/orientdb/issues/7985 TODO remove the CONNECT & CREATE CLASS commands when it will be fixed
$ORIENTDB_PATH/bin/console.sh "CREATE DATABASE remote:$ORIENTDB_HOST/debattons root $ORIENTDB_ROOT_PASSWORD PLOCAL; CREATE USER \`api-server\` IDENTIFIED BY password ROLE admin; CONNECT remote:$ORIENTDB_HOST/debattons api-server password; CREATE CLASS Root EXTENDS V" || echo "Already created"

if [ -z "$ONLY_START_ORIENTDB_SERVER" ]; then
  export DEBATTONS_DATABASE_URL=$ORIENTDB_HOST/debattons
  cd /opt/debattons/api-server && mvn spring-boot:run &
  cd /opt/debattons/ui && \
      yarn install --no-bin-links && # no bin links else we got "EPROTO: protocol error, symlink '../../../../less/bin/lessc' -> '/vagrant/ui/node_modules/@angular/cli/node_modules/.bin/lessc'" with Vagrant on Windows thanks to https://github.com/npm/npm/issues/9901#issuecomment-146585579 \
      chmod +x ./node_modules/@angular/cli/bin/ng && # to avoid "Permission denied" when executing the following line \
      ./node_modules/@angular/cli/bin/ng serve --host 0.0.0.0 # authorize remote access thanks to https://github.com/angular/angular-cli/issues/1793#issuecomment-241343672
else
  tail -f $ORIENTDB_PATH/log/*
fi
