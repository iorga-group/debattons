#!/bin/bash

ln -sf /vagrant /opt/debattons
su - ubuntu -c "/opt/debattons/setup/dev-env-on-ubuntu-16.04.sh --no-debattons-git-copy"
su - ubuntu -c "/bin/bash /opt/debattons/scripts/build-and-run.dev.sh --start-orientdb-server"

