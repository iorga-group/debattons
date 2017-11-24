#!/bin/bash

set -x
set -e

# Configure proxy in case it is set thanks to http://www.jhipster.tech/configuring-a-corporate-proxy/ & inspired by https://github.com/anthony-o/transmart-docker/blob/sanofi-release-16.1/servers/cmd.sh
rm /tmp/settings.in.xml || echo "settings.in.xml didn't exist"
for PROTO in HTTPS_PROXY HTTP_PROXY ; do
    # lower case in bash thanks to http://stackoverflow.com/a/2264537/535203
    LOWER_PROTO=$(echo $PROTO | tr '[:upper:]' '[:lower:]')
    # dynamic variable name thanks to http://stackoverflow.com/a/18124325/535203
    PROXY_VALUE=${!PROTO:-${!LOWER_PROTO}}
    
    # Testing priority in bash thanks to http://wiki.bash-hackers.org/commands/classictest
    if [ -n "$PROXY_VALUE" ] ; then
        # Configuring Maven proxy
        PROXY_VALUE_HOST=`echo $PROXY_VALUE | sed -e's,^.*://\(.*\):.*,\1,g'`
        PROXY_VALUE_PORT=`echo $PROXY_VALUE | sed -e's,^.*://.*:\(.*\),\1,g'`
        if [ ! -f ~/.m2/settings.xml ] || ! grep "$PROXY_VALUE_HOST" ~/.m2/settings.xml ; then
            cat >>/tmp/settings.in.xml <<EOF
    <proxy>
        <id>$PROTO</id>
        <active>true</active>
        <protocol>`echo $PROXY_VALUE | sed -e's,^\(.*\)://.*,\1,g'`</protocol>
        <host>$PROXY_VALUE_HOST</host>
        <port>$PROXY_VALUE_PORT</port>
        <nonProxyHosts>$PROXY_VALUE_HOST</nonProxyHosts>
    </proxy>
EOF
        fi
        # Configuring Yarn proxy
        YARN_PROXY=proxy
        if [ $PROTO = "HTTPS" ]; then
            YARN_PROXY=https-proxy
        fi
        sudo yarn config set $YARN_PROXY "$PROXY_VALUE"
        yarn config set $YARN_PROXY "$PROXY_VALUE"
    fi
done
if [ -f /tmp/settings.in.xml ] ; then
    mkdir -p ~/.m2 # In case does not exist, to avoid "No such file or directory" error
    cat >~/.m2/settings.xml <<EOF
<settings><proxies>
`cat /tmp/settings.in.xml`
</proxies></settings>
EOF
    rm /tmp/settings.in.xml
fi