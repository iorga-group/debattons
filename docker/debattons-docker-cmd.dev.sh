#!/usr/bin/env bash

set -e
set -x

# Creating the api-server user if not exist
while ! nc -vz orientdb 2424; do
    sleep 1
done

/opt/orientdb/bin/console.sh "CREATE DATABASE remote:orientdb/debattons root $ORIENTDB_ROOT_PASSWORD PLOCAL; CREATE USER api-server IDENTIFIED BY password ROLE admin;" || echo "Already created"

# Setting up the proxy for Maven if needed, inspired by https://github.com/anthony-o/transmart-docker/blob/sanofi-release-16.1/servers/cmd.sh
rm /tmp/settings.in.xml || echo "settings.in.xml didn't exist"
for PROTO in HTTPS_PROXY HTTP_PROXY ; do
    # lower case in bash thanks to http://stackoverflow.com/a/2264537/535203
    LOWER_PROTO=$(echo $PROTO | tr '[:upper:]' '[:lower:]')
    # dynamic variable name thanks to http://stackoverflow.com/a/18124325/535203
    PROXY_IN_CONTAINER=${!PROTO:-${!LOWER_PROTO}}
    
    # Testing priority in bash thanks to http://wiki.bash-hackers.org/commands/classictest
    if [ -n "$PROXY_IN_CONTAINER" ] ; then
        PROXY_IN_CONTAINER_HOST=`echo $PROXY_IN_CONTAINER | sed -e's,^.*://\(.*\):.*,\1,g'`
        PROXY_IN_CONTAINER_PORT=`echo $PROXY_IN_CONTAINER | sed -e's,^.*://.*:\(.*\),\1,g'`
        if [ ! -f ~/.m2/settings.xml ] || ! grep "$PROXY_IN_CONTAINER_HOST" ~/.m2/settings.xml ; then
            cat >>/tmp/settings.in.xml <<EOF
    <proxy>
        <id>$PROTO</id>
        <active>true</active>
        <protocol>`echo $PROXY_IN_CONTAINER | sed -e's,^\(.*\)://.*,\1,g'`</protocol>
        <host>$PROXY_IN_CONTAINER_HOST</host>
        <port>$PROXY_IN_CONTAINER_PORT</port>
        <nonProxyHosts>$PROXY_IN_CONTAINER_HOST</nonProxyHosts>
    </proxy>
EOF
        fi
    fi
done
if [ -f /tmp/settings.in.xml ] ; then
    cat >~/.m2/settings.xml <<EOF
<settings><proxies>
`cat /tmp/settings.in.xml`
</proxies></settings>
EOF
    rm /tmp/settings.in.xml
fi

export DEBATTONS_DATABASE_URL=orientdb/debattons
cd /opt/debattons/api-server && mvn spring-boot:run &
cd /opt/debattons/ui && npm install && ng serve --host 0.0.0.0 # authorize remote access thanks to https://github.com/angular/angular-cli/issues/1793#issuecomment-241343672
