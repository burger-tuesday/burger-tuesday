module.exports = {
  webpack: function (config, env) {
    if (process.env.CI) {
      config.plugins = (config.plugins || [])
        .concat([new SentryCliPlugin({})]);
    }
    return config;
  }
}
