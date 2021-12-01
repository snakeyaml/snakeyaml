#!/usr/bin/env bash
docker run --rm -it               \
    -u `id -u`:`id -g`            \
    -v `pwd`:/work                \
    -v ~:/my-home                 \
    -e "HOME=/my-home"            \
    -w /work                      \
    $1                            \
    ./mvnw -Dmaven.repo.local=/my-home/.m2/repository clean test ${@:2}

