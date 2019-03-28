#!/usr/bin/env bash
set -e
set -x

if [ -f "$HOME/.gitconfig" ]; then
    GITCONFIG=(-v "$HOME/.gitconfig:/home/jhipster/.gitconfig:ro")
    # We put the previous line in an array (using parenthesis) in order to split the elements using space and keep the potential spaces present in $HOME in the second element of the array (thanks to merci à https://unix.stackexchange.com/a/131767/29674 )
fi

CURRENT_GID=`id -g`
CURRENT_UID=`id -u`
CHANGE_USER_CMD=""

if [[ "$CURRENT_GID" != 1000 ]]; then
    CHANGE_USER_CMD="$CHANGE_USER_CMD && sed -i s/:1000:/:$CURRENT_GID:/ /etc/group"
fi
if [[ "$CURRENT_UID" != 1000 ]]; then
    CHANGE_USER_CMD="$CHANGE_USER_CMD && sed -i s/1000:1000/$CURRENT_UID:$CURRENT_GID/ /etc/passwd"
fi
if [[ -n "$CHANGE_USER_CMD" ]]; then
    CHANGE_USER_CMD="$CHANGE_USER_CMD && chown -R jhipster:jhipster /usr/local/lib/node_modules /home/jhipster/{.bash*,generator-jhipster} && chown jhipster:jhipster /home/jhipster"
fi

docker run -it --rm --name jhipster -v "$(pwd):/home/jhipster/app" -v ~/.m2:/home/jhipster/.m2 -v ~/.cache/yarn:/home/jhipster/.cache/yarn "${GITCONFIG[@]}" -u root jhipster/jhipster:`cat $(dirname "$0")/jhipster_version.txt` bash -c "set -x $CHANGE_USER_CMD && su -l jhipster -c 'cd app && $*'"
