#!/bin/sh
set -e
./gradlew --no-daemon build jacocoTestReport jib
curl -sL https://sentry.io/get-cli/ | bash
sentry-cli releases new --finalize $TRAVIS_COMMIT
sentry-cli releases set-commits $TRAVIS_COMMIT --auto
