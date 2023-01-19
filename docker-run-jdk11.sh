#!/usr/bin/env bash
./run-in-docker.sh eclipse-temurin:11 -Pwith-java11-tests -Preformat $@
