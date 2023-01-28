import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {

  // Your Auth0 app's domain
  // Important: Don't forget to start with https://
  //  AND the trailing slash!
  issuer: 'http://localhost:8090',

  // The app's redirectUri configured in Auth0
  redirectUri: window.location.origin ,

  // URL of the SPA to redirect the user after silent refresh
  silentRefreshRedirectUri: window.location.origin,

  useSilentRefresh: true,

  // The app's clientId configured in Auth0 - example client id
  clientId: 'fea08f772bb001a15162',

  // Scopes ("rights") the Angular application wants get delegated
  scope: '',

  // Using Authorization Code Flow
  // (PKCE is activated by default for authorization code flow)
  responseType: 'code',

  // Your Auth0 account's logout url
  // Derive it from your application's domain
  //logoutUrl: 'https://id.company-name.com/logout',

  customQueryParams: {
    // API identifier configured in Auth0
    audience: 'https://dev-51246k0z.us.auth0.com/api/v2/',
  },

  silentRefreshTimeout: 5000, // For faster testing
  timeoutFactor: 0.25, // For faster testing
  sessionChecksEnabled: true,
  showDebugInformation: true, // Also requires enabling "Verbose" level in devtools
  clearHashAfterLogin: false, // https://github.com/manfredsteyer/angular-oauth2-oidc/issues/457#issuecomment-431807040,
  nonceStateSeparator : 'semicolon' // Real semicolon gets mangled by IdentityServer's URI encoding
};
