#!/bin/bash

set -x
set -e

sudo -E apt-get update

sudo -E apt-get install -y git
cd /tmp
git clone https://github.com/iorga-group/debattons
sudo mv debattons /opt/