#!/usr/bin/env bash

docker run --rm -it               \
    -v `pwd`:/work                \
    -v ~:/my-home                 \
    -e "HOME=/my-home"            \
    -w /work                      \
    $1                            \
    ./mvnw -Dmaven.repo.local=/my-home/.m2/repository clean install site -Pwith-java11-tests ${@:2}
