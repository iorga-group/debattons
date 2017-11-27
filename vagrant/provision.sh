#!/bin/bash

ln -nfs /vagrant /opt/debattons
mkdir -p /vagrant/vagrant-data/{.m2,databases}
ln -nfs /vagrant/vagrant-data/.m2 /home/ubuntu/.m2
su - ubuntu -c "/opt/debattons/setup/dev-env-on-ubuntu-16.04.sh --no-debattons-git-copy"
ln -nfs /vagrant/vagrant-data/databases /opt/orientdb/databases
su - ubuntu -c "/bin/bash /opt/debattons/scripts/build-and-run.dev.sh --start-orientdb-server"
