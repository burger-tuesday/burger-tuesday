const SentryCliPlugin = require('@sentry/webpack-plugin');

module.exports = {
  webpack: function (config, env) {
    if (process.env.UPLOAD_SOURCEMAPS) {
      config.plugins = (config.plugins || [])
        .concat([new SentryCliPlugin({include: '.', release: process.env.SENTRY_RELEASE})]);
    }

    return config;
  }
};
