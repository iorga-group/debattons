#!/usr/bin/env bash
set -e
set -x

if [ -f "$HOME/.gitconfig" ]; then
    GITCONFIG=(-v "$HOME/.gitconfig:/home/jhipster/.gitconfig:ro")
    # We put the previous line in an array (using parenthesis) in order to split the elements using space and keep the potential spaces present in $HOME in the second element of the array (thanks to merci à https://unix.stackexchange.com/a/131767/29674 )
fi

JHIPSTER_VERSION=`cat $(dirname "$0")/jhipster_version.txt`
JHIPSTER_IMAGE="jhipster/jhipster:$JHIPSTER_VERSION"

CURRENT_GID=`id -g`
CURRENT_UID=`id -u`

if [[ "$CURRENT_GID" != 1000 || "$CURRENT_UID" != 1000 ]]; then
    NEW_JHIPSTER_IMAGE="${JHIPSTER_IMAGE}_u${CURRENT_UID}_g$CURRENT_GID"

    # Check the existance of this image or build it thanks to https://stackoverflow.com/a/33061675/535203
    if ! docker inspect --type=image $NEW_JHIPSTER_IMAGE >/dev/null 2>/dev/null; then
        if [[ "$CURRENT_UID" != 1000 ]]; then
            RUN_USERMOD="usermod -u $CURRENT_UID jhipster && find / -user 1000 -exec chown -h $CURRENT_UID {} \\;;"
        fi
        if [[ "$CURRENT_GID" != 1000 ]]; then
            RUN_GROUPMOD="groupmod -g $CURRENT_GID jhipster && find / -group 1000 -exec chgrp -h $CURRENT_GID {} \\;; usermod -g $CURRENT_GID jhipster"
        fi
        docker build -t $NEW_JHIPSTER_IMAGE - <<EOF
FROM $JHIPSTER_IMAGE
# Change UID & GID of jhipster thanks to https://muffinresearch.co.uk/linux-changing-uids-and-gids-for-user/
USER root
RUN $RUN_USERMOD $RUN_GROUPMOD
USER jhipster
EOF
    fi

    JHIPSTER_IMAGE=$NEW_JHIPSTER_IMAGE
fi

docker run -it --rm --name jhipster -v "$(pwd):/home/jhipster/app" -v ~/.m2:/home/jhipster/.m2 -v ~/.cache/yarn:/home/jhipster/.cache/yarn "${GITCONFIG[@]}" $JHIPSTER_IMAGE "$@"
