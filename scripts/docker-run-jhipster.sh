#!/usr/bin/env bash
set -e
set -x

if [ -f "$HOME/.gitconfig" ]; then
    GITCONFIG=(-v "$HOME/.gitconfig:/home/jhipster/.gitconfig:ro")
    # We put the previous line in an array (using parenthesis) in order to split the elements using space and keep the potential spaces present in $HOME in the second element of the array (thanks to merci à https://unix.stackexchange.com/a/131767/29674 )
fi

docker run -it --rm --name jhipster -v "$(pwd):/home/jhipster/app" -v ~/.m2:/home/jhipster/.m2 -v ~/.cache/yarn:/home/jhipster/.cache/yarn "${GITCONFIG[@]}" jhipster/jhipster:`cat $(dirname "$0")/jhipster_version.txt` "$@"
