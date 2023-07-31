#!/bin/zsh

./gradlew shadowJar

sls deploy --aws-profile keyme