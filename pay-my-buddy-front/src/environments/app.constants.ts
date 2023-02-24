
export class AppConstants {
  private static API_BASE_URL = "http://localhost:8090/";
  public static API_URL = AppConstants.API_BASE_URL + "api/";
  private static OAUTH2_URL = AppConstants.API_URL + "oauth2/authorization/";
  private static OAUTH2_CODE_URL = AppConstants.API_URL + "login/oauth2/code/";
  private static REDIRECT_URL = "?redirect_uri=http://localhost:4200/login";
  public static AUTH_API = AppConstants.API_URL + "auth/";
  public static GOOGLE_AUTH_URL = AppConstants.OAUTH2_URL + "google" + AppConstants.REDIRECT_URL;
  public static GITHUB_AUTH_URL = AppConstants.OAUTH2_URL + "github" + AppConstants.REDIRECT_URL;

  public static GITHUB_CODE_URL = AppConstants.OAUTH2_CODE_URL + "github";
  public static GOOGLE_CODE_URL = AppConstants.OAUTH2_CODE_URL + "google";

}
