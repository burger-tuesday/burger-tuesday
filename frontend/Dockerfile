# build environment
FROM node:11 as builder
ARG UPLOAD_SOURCEMAPS
ARG SENTRY_AUTH_TOKEN
ARG SENTRY_RELEASE
RUN mkdir /usr/src/app
WORKDIR /usr/src/app
ENV PATH /usr/src/app/node_modules/.bin:$PATH
COPY package.json /usr/src/app/package.json
COPY yarn.lock /usr/src/app/yarn.lock
RUN yarn install
COPY . /usr/src/app
RUN yarn build
RUN /usr/src/app/node_modules/.bin/sentry-cli releases set-commits $SENTRY_RELEASE --commit "burger-tuesday/burger-tuesday@$SENTRY_RELEASE"

# production environment
FROM nginx:1.15-alpine
COPY --from=builder /usr/src/app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
COPY nginx.vh.default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
