#!/usr/bin/env bash

set -e
set -x

# Creating the api-server user if not exist
while ! nc -vz orientdb 2424; do
  sleep 1
done

/opt/orientdb/bin/console.sh "CREATE DATABASE remote:orientdb/debattons root $ORIENTDB_ROOT_PASSWORD PLOCAL; CREATE USER api-server IDENTIFIED BY password ROLE admin;" || echo "Already created"

export DEBATTONS_DATABASE_URL=orientdb/debattons
cd /opt/debattons/api-server && mvn spring-boot:run &
cd /opt/debattons/ui && npm install && ng serve --host 0.0.0.0 # authorize remote access thanks to https://github.com/angular/angular-cli/issues/1793#issuecomment-241343672
