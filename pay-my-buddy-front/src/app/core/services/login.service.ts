import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppConstants} from "../../../environments/app.constants";
import {tap} from "rxjs/operators";
import {AuthService} from "./auth.service";
import  { CookieService } from 'ngx-cookie-service';

const httpOptions = {
  headers: new HttpHeaders(
    {
      'Content-Type': 'application/json',
    }
  )
};

const httpText = {
  headers: new HttpHeaders(
    {
      'Content-Type': 'application/text',
    }
  )
};

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  providerSelected : string = "";

  constructor(private http: HttpClient,
              private  authService: AuthService,
              private  cookieService: CookieService) {

  }
  connectGithub(){
    this.providerSelected = "github";
    window.open(
      AppConstants.GITHUB_AUTH_URL
      , '_self');

  }

  connectGoogle(){
    this.providerSelected = "google";
    window.open(
      AppConstants.GOOGLE_AUTH_URL
      , '_self');
  }


  fetchTokenGithub(code : string, state : string): Observable<any> {
    return this.http.get(
      AppConstants.GITHUB_CODE_URL + '?code=' + code + '&state=' + state + "&redirect_uri=http://localhost:4200/login",
      {
        withCredentials: true,
        observe: 'response',
      })
      .pipe(tap((r) => {console.log("TOKENNN : ", r.headers.get('Token')),
      this.authService.createSession(r.headers.get('Token'))
      }));
  }

  fetchTokenGoogle(code : string, state : string, scope: string, authuser: string, prompt: string): Observable<any> {
    return this.http.get(
      AppConstants.GOOGLE_CODE_URL + '?state=' + state + '&code=' + code + '&scope=' + scope + '&authuser=' + authuser + '&prompt=' + prompt,
      {
        withCredentials: true,
        observe: 'response',
      })
      .pipe(tap((r) => {console.log("TOKENNN : ", r.headers.get('Token')),
        this.authService.createSession(r.headers.get('Token'))
      }));
  }

}
