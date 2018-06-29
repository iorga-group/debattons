FROM orientdb:3.0.2

RUN apk add --no-cache bash

ENV ORIENTDB_PATH /orientdb

RUN mkdir -p /opt/debattons/setup
ADD scripts/build-and-run.dev.sh /usr/local/bin/build-and-run.dev.sh

CMD build-and-run.dev.sh --only-start-orientdb-server
