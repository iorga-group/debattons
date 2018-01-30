#!/bin/bash

ln -nfs /vagrant /opt/debattons
for DIR in .m2 .cache; do
  mkdir -p /vagrant/vagrant-data/$DIR
  ln -nfs /vagrant/vagrant-data/$DIR /home/ubuntu/$DIR
done
su - ubuntu -c "/opt/debattons/setup/run-env-on-ubuntu-16.04.sh --no-debattons-git-copy"
su - ubuntu -c "/opt/debattons/docker/cmd.sh build-and-run"
