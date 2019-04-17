#!/bin/sh
echo "$DOCKER_PASSWORD" | docker login -u "pdgwien" --password-stdin
yarn test --coverage
docker build -t "$CONTAINER_NAME:latest" .
docker tag "$CONTAINER_NAME:latest" "$CONTAINER_NAME:$TRAVIS_BUILD_NUMBER" && docker push "$CONTAINER_NAME"
