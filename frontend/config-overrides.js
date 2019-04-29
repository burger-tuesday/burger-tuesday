const SentryCliPlugin = require('@sentry/webpack-plugin');

module.exports = {
  webpack: function (config, env) {
    if (process.env.CI) {
      config.plugins = (config.plugins || [])
      .concat([new SentryCliPlugin({include: '.'})]);
    }

    return config;
  }
};
