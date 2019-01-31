#!/usr/bin/env bash
set -e
set -x

cd "$(dirname "$0")/.."

./scripts/docker-run-jhipster.sh jhipster import-jdl ./scripts/debattons.jdl --force
