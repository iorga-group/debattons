#!/usr/bin/env bash

set -e
set -x

PROJECT_NAME=debattons

BASE_DIR="$DIR_PREFIX$(readlink -f `dirname $0`)"
export DOCKER_DATA=$(readlink -f "$BASE_DIR/../docker-data")
DOCKER_COMPOSE=(docker-compose -f "$BASE_DIR/docker-compose.yml" -p $PROJECT_NAME)

COMMAND=$1
shift

generate_password() {
	# Generate a password thanks to https://gist.github.com/earthgecko/3089509 and https://unix.stackexchange.com/a/230676/29674
	cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w $1 | head -n 1
}

if [ "$COMMAND" == "build-and-run" ] ; then
	SERVICES_TO_START="orientdb"

	while [ "$#" != "0" ] ; do
		if [ "$1" == "--only-orientdb" ] ; then
			SERVICES_TO_START="orientdb"
		fi
		shift
	done

	if [ ! -f "$DOCKER_DATA/conf/$PROJECT_NAME.env" ] ; then
		mkdir -p "$DOCKER_DATA/conf"
		ENV_FILE="$DOCKER_DATA/conf/$PROJECT_NAME.env"
		echo "export ORIENTDB_ROOT_PASSWORD=$(generate_password 12)" >> "$ENV_FILE"
	fi
	source "$DOCKER_DATA/conf/$PROJECT_NAME.env"

  # Using "${DOCKER_COMPOSE[@]}" in order to handle correctly paths with spaces in it thanks to https://stackoverflow.com/a/1555811/535203
	"${DOCKER_COMPOSE[@]}" build
	"${DOCKER_COMPOSE[@]}" down --volumes
	"${DOCKER_COMPOSE[@]}" up -d --force-recreate $SERVICES_TO_START
elif [ "$COMMAND" == "stop-and-remove" ] ; then
	"${DOCKER_COMPOSE[@]}" down --volumes
fi

