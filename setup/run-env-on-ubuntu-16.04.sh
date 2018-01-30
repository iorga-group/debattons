#!/bin/bash

set -x
set -e

sudo -E apt-get update

# Install Docker, following the official documentation https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/#install-using-the-repository
sudo -E apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo -E apt-key add -

## Checking the Docker footprint
apt-key fingerprint 0EBFCD88 | grep "9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88" -A 1 | grep "Docker Release (CE deb) <docker@docker.com>"
sudo -E add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
sudo -E apt-get update
## To check the available versions : apt-cache madison docker-ce
sudo -E apt-get install -y docker-ce=17.12.0~ce-0~ubuntu
sudo usermod -aG docker $USER # adding current user to "docker" group in order to execute docker without sudo thanks to https://askubuntu.com/a/739861/29219
## Configuring the proxy if any following the documentation https://docs.docker.com/engine/admin/systemd/#httphttps-proxy
if [ -n "$HTTP_PROXY" ] || [ -n "$http_proxy" ]; then
    sudo mkdir -p /etc/systemd/system/docker.service.d
    sudo tee /etc/systemd/system/docker.service.d/http-proxy.conf<<EOF
[Service]
Environment="HTTP_PROXY=${HTTP_PROXY:-$http_proxy}"
EOF
    PROXY_CONFIGURED=true
fi
if [ -n "$HTTPS_PROXY" ] || [ -n "$https_proxy" ]; then
    sudo mkdir -p /etc/systemd/system/docker.service.d
    sudo tee /etc/systemd/system/docker.service.d/https-proxy.conf<<EOF
[Service]
Environment="HTTPS_PROXY=${HTTPS_PROXY:-$https_proxy}"
EOF
    PROXY_CONFIGURED=true
fi
if [ -n "$PROXY_CONFIGURED" ]; then
    sudo systemctl daemon-reload
    sudo systemctl restart docker
fi
## Checking that docker works
sg docker "docker run --rm hello-world" | grep "Hello from Docker!" # we use sg in order to avoid the execution of a new session to take into account the group modification thanks to https://askubuntu.com/a/469391/29219

# Install Docker-Compose
sudo -E curl -L https://github.com/docker/compose/releases/download/1.16.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
## Checking docker-compose version
docker-compose --version | grep "docker-compose version 1.16.1, build 6d1ac21"

if [ "$1" != "--no-debattons-git-copy" ]; then
    # Checkout git repository of Debattons
    BASE_DIR="$(readlink -f `dirname $0`)"
    if [ -f "$BASE_DIR/debattons-git-copy.sh" ]; then
        source "$BASE_DIR/debattons-git-copy.sh"
    else
        curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/debattons-git-copy.sh" > /tmp/setup-debattons-git-copy.sh && bash /tmp/setup-debattons-git-copy.sh
        rm /tmp/setup-debattons-git-copy.sh
    fi
fi
