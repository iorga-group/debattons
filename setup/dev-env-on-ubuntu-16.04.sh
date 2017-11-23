#!/bin/bash

set -x
set -e

BASE_DIR="$(readlink -f `dirname $0`)"

if [ "$1" != "--no-run-env" ]; then
    if [ -f "$BASE_DIR/run-env-on-ubuntu-16.04.sh" ]; then
        source "$BASE_DIR/run-env-on-ubuntu-16.04.sh"
    else
        sudo -E apt-get install -y curl
        curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/run-env-on-ubuntu-16.04.sh" > /tmp/setup-debattons-run-env-on-ubuntu-16.04.sh && bash /tmp/setup-debattons-run-env-on-ubuntu-16.04.sh
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

# Install angular-cli
sudo -E npm install -g @angular/cli@1.5.3

# Install Yarn, following the documentation https://yarnpkg.com/en/docs/install#linux-tab
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo -E apt-get update && sudo -E apt-get install yarn