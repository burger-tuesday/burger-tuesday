export const AUTH_CONFIG = {
  audience: process.env.REACT_APP_AUTH_AUDIENCE || '',
  callbackUrl: process.env.REACT_APP_AUTH_CALLBACK_URL || '',
  clientId: process.env.REACT_APP_AUTH_CLIENT_ID || '',
  domain: process.env.REACT_APP_AUTH_DOMAIN || ''
};
