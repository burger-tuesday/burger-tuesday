import * as auth0 from 'auth0-js';
import {Auth0DecodedHash, Auth0Error, Auth0UserProfile, WebAuth} from 'auth0-js';
import {action, computed, observable, reaction} from 'mobx';
import history from '../history';
import {AUTH_CONFIG} from './auth-variables';
import commonStore from './commonStore';

export interface IAuthStore {
  readonly accessToken: string | null;
  readonly idToken: string | null;
  readonly expiresAt: number;
  readonly isAuthenticated: boolean;
  readonly userProfile: Auth0UserProfile | null;
  login(): void;
  handleAuthentication(): void;
  renewSession(): void;
  logout(): void;
}

class AuthStore implements IAuthStore {
  @observable public accessToken: string | null;
  @observable public idToken: string | null;
  @observable public expiresAt: number;
  @observable public userProfile: Auth0UserProfile | null;
  private auth: WebAuth;

  constructor() {
    this.auth = new auth0.WebAuth({
      audience: AUTH_CONFIG.audience,
      clientID: AUTH_CONFIG.clientId,
      domain: AUTH_CONFIG.domain,
      redirectUri: AUTH_CONFIG.callbackUrl,
      responseType: 'id_token token',
      scope: 'openid profile email read:burgers manage:burgers test:something'
    });

    reaction(
        () => this.accessToken,
        accessToken => {
          this.getProfile();
        }
    );
  }

  @action
  public login() {
    this.auth.authorize();
  }

  @action
  public handleAuthentication() {
    this.auth.parseHash((err, authResult) => {
      if (authResult && authResult.accessToken && authResult.idToken) {
        this.setSession(authResult);
      } else if (err) {
        history.replace('/home');
        console.log(err);
        alert(`Error: ${err.error}. Check the console for further details.`);
      }
    });
  }


  @action
  public renewSession() {
    this.auth.checkSession({}, (err, authResult) => {
      if (authResult && authResult.accessToken && authResult.idToken) {
        this.setSession(authResult);
      } else if (err) {
        this.logout();
        console.log(err);
        alert(`Could not get a new token (${err.error}: ${err.errorDescription}).`);
      }
    });
  }

  @action
  public getProfile() {
    if (this.accessToken == null) {
      this.userProfile = null;
      return;
    }
    this.auth.client.userInfo(this.accessToken, (err: Auth0Error, profile: Auth0UserProfile) => {
      if (profile) {
        this.setUserProfile(profile);
      } else if (err) {
        console.log(err);
      }
    });
  }

  @action
  public logout() {
    // Remove tokens and expiry time
    this.accessToken = null;
    this.idToken = null;
    this.expiresAt = 0;

    commonStore.setLoggedIn(false);

    this.auth.logout({
      returnTo: window.location.origin
    });

    // navigate to the home route
    history.replace('/home');
  }

  @computed get isAuthenticated(): boolean {
    // Check whether the current time is past the
    // access token's expiry time
    return new Date().getTime() < this.expiresAt
  }

  @action
  private setSession(authResult: Auth0DecodedHash) {
    commonStore.setLoggedIn(true);

    // Set the time that the access token will expire at
    const {idToken = null, accessToken = null, expiresIn = 0} = authResult;
    const expiresAt = (expiresIn * 1000) + new Date().getTime();
    this.accessToken = accessToken;
    this.idToken = idToken;
    this.expiresAt = expiresAt;

    // navigate to the home route
    history.replace('/home');
  }

  @action
  private setUserProfile(profile: Auth0UserProfile) {
    this.userProfile = profile;
  }
}

export default new AuthStore();
