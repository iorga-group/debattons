#!/bin/bash

set -x
set -e

sudo -E apt-get update

BASE_DIR="$(readlink -f `dirname $0`)"

if [ "$1" != "--no-debattons-git-copy" ]; then
    if [ -f "$BASE_DIR/debattons-git-copy.sh" ]; then
        source "$BASE_DIR/debattons-git-copy.sh"
    else
        curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/debattons-git-copy.sh" > /tmp/setup-debattons-git-copy.sh && bash /tmp/setup-debattons-git-copy.sh
        rm /tmp/setup-debattons-git-copy.sh
    fi
fi

# Install JDK8
sudo -E apt-get install -y openjdk-8-jdk openjdk-8-source

# Install Maven
sudo -E apt-get install -y maven

# Install NodeJS, following official documentation https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
sudo -E apt-get install -y curl
curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash -
sudo -E apt-get install -y nodejs build-essential

# Install Yarn, following the documentation https://yarnpkg.com/en/docs/install#linux-tab
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo -E apt-get update && sudo -E apt-get install yarn

# Configure proxy in case it is set
/opt/debattons/setup/proxy.sh

# Install wget
sudo -E apt-get install -y wget

# Install OrientDB tools (inspired by the Docker script https://github.com/orientechnologies/orientdb-docker/blob/e6855db21e0c4eb47db36f60a7b8a4d05ef5ffb4/3.0/x86_64/alpine/Dockerfile)
ORIENTDB_VERSION=3.0.0RC1
ORIENTDB_DOWNLOAD_MD5=f231735d7ac8c18e8e5f6aab5e6ae459
ORIENTDB_DOWNLOAD_SHA1=8a1d8d5468d55bdb9430f525481ba356e9ca6209

ORIENTDB_DOWNLOAD_URL=${ORIENTDB_DOWNLOAD_SERVER:-http://central.maven.org/maven2/com/orientechnologies}/orientdb-community-gremlin/$ORIENTDB_VERSION/orientdb-community-gremlin-$ORIENTDB_VERSION.tar.gz

#download distribution tar, untar and delete databases
cd /tmp && mkdir orientdb && \
  wget  $ORIENTDB_DOWNLOAD_URL \
  && echo "$ORIENTDB_DOWNLOAD_MD5 *orientdb-community-gremlin-$ORIENTDB_VERSION.tar.gz" | md5sum -c - \
  && echo "$ORIENTDB_DOWNLOAD_SHA1 *orientdb-community-gremlin-$ORIENTDB_VERSION.tar.gz" | sha1sum -c - \
  && tar -xvzf orientdb-community-gremlin-$ORIENTDB_VERSION.tar.gz -C /tmp/orientdb --strip-components=1 \
  && rm orientdb-community-gremlin-$ORIENTDB_VERSION.tar.gz \
  && rm -rf /tmp/orientdb/databases/* \
  && sudo mv /tmp/orientdb /opt/

# Install build-and-run.dev.sh dependencies
sudo -E apt-get install -y netcat
