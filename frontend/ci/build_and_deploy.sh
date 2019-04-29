#!/bin/sh
echo "$DOCKER_PASSWORD" | docker login -u "pdgwien" --password-stdin
yarn test --coverage
docker build --build-arg UPLOAD_SOURCEMAPS=true --build-arg SENTRY_AUTH_TOKEN=$SENTRY_AUTH_TOKEN -t "$CONTAINER_NAME:latest" .
docker tag "$CONTAINER_NAME:latest" "$CONTAINER_NAME:$TRAVIS_BUILD_NUMBER" && docker push "$CONTAINER_NAME"
