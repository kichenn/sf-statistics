#!/bin/bash
set -e
REPO=docker-reg.emotibot.com.cn:55688
CONTAINER=sf-statistics
# FIXME: should use some tag other than latest
TAG=$(date '+%Y%m%d')-$(git rev-parse --short HEAD)
DOCKER_IMAGE=$REPO/$CONTAINER:$TAG

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILDROOT=$DIR/..

#mvn clean install -D maven.test.skip=true
cmd="cd ../ && mvn clean && mvn package -DskipTests"
bash -c "$cmd"

# Build docker --no-cache
cmd="docker build -t $DOCKER_IMAGE -f $DIR/Dockerfile $BUILDROOT"
echo $cmd
eval $cmd

#cmd="docker push $REPO/$CONTAINER"
#echo $cmd
#eval $cmd
