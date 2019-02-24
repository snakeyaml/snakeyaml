#!/usr/bin/env bash
docker run --rm -it               \
    -u `id -u`:`id -g`            \
    -v `pwd`:/work                \
    -v ~:/my-home                 \
    -e "HOME=/my-home"            \
    -w /work                      \
    maven:3.5.4-jdk-$1            \
    mvn -Dmaven.repo.local=/my-home/.m2/repository clean test ${@:2}
