import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppConstants} from "../../../environments/app.constants";
import {tap} from "rxjs/operators";
import {AuthService} from "./auth.service";

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

  constructor(private http: HttpClient, private  authService: AuthService) {

  }
  connectGithub() : Observable<any>{
    return this.http.get<any>(
      AppConstants.GITHUB_AUTH_URL,
      httpOptions);

  }


  fetchToken(code : string, state : string): Observable<any> {
    return this.http.get(
      AppConstants.GITHUB_CODE_URL + '?code=' + code + '&state=' + state,
      {observe: 'response'})
      .pipe(tap((r) => {console.log("TOKENNN : ", r.headers.get('Token')),
      this.authService.createSession(r.headers.get('Token'))
      }));
  }

  connectPage() {
    return this.http.get<any>(
      `http://localhost:8090/login`,
      httpOptions);
  }
}
