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
/opt/debattons/setup/proxy-on-ubuntu.sh

# Install angular-cli
sudo -E yarn global add @angular/cli@1.5.3