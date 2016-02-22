#!/usr/bin/env bash
if [ "$#" -ne 1 ]
then
  echo "Usage: docker-run.sh <JDK number> (number can be 6, 7, 8)"
  exit 1
fi

docker run --rm -it               \
    -u `id -u`:`id -g`            \
    -v `pwd`:/work                \
    -v ~:/my-home                 \
    -e "HOME=/my-home"            \
    -w /work                      \
    maven:3-jdk-$1                \
    mvn -Dmaven.repo.local=/my-home/.m2/repository clean test

