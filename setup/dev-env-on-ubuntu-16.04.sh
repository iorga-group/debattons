#!/bin/bash

set -x
set -e

apt-get update

# Install JDK8
apt-get install -y openjdk-8-jdk openjdk-8-source

# Install Maven
apt-get install -y maven

# Install NodeJS, following official documentation https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
apt-get install -y curl
curl -sL https://deb.nodesource.com/setup_6.x | bash -
apt-get install -y nodejs build-essential

# Install Yarn, following the documentation https://yarnpkg.com/en/docs/install#linux-tab
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list
apt-get update && apt-get install yarn

# Configure proxy in case it is set
#/opt/debattons/setup/proxy-on-ubuntu.sh

# Install angular-cli
yarn global add @angular/cli@1.5.3

# Install OrientDB tools (inspired by the Docker script https://github.com/orientechnologies/orientdb-docker/blob/222b64299884eeb4b324e2822873f69e8a7c006e/3.0/x86_64/alpine/Dockerfile)
## Using latest 3.0.0-SNAPSHOT as a bug in the console was fixed from 3.0.0m2 version, and we need this fix https://stackoverflow.com/a/47469583/535203 / https://github.com/orientechnologies/orientdb/issues/7898
ORIENTDB_VERSION=3.0.0-SNAPSHOT

cd /tmp && mkdir orientdb && \
mvn dependency:get -Ddest=./ -DremoteRepositories=sonatype-nexus-snapshots::::https://oss.sonatype.org/content/repositories/snapshots -Dartifact=com.orientechnologies:orientdb-community:$ORIENTDB_VERSION:tar.gz \
&& tar -xvzf orientdb-community-$ORIENTDB_VERSION.tar.gz -C /tmp/orientdb --strip-components=1 \
&& rm orientdb-community-$ORIENTDB_VERSION.tar.gz

sudo mv /tmp/orientdb /opt/

# Install build-and-run.dev.sh dependencies
sudo -E apt-get install -y netcat
chown ubuntu.ubuntu /opt/orientdb -R

ln -sf /vagrant /opt/debattons
su - ubuntu -c "/bin/bash /opt/debattons/scripts/build-and-run.dev.sh --start-orientdb-server"

