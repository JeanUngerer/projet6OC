import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppConstants} from "../../../environments/app.constants";

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

  constructor(private http: HttpClient) {

  }
  connectGithub() : Observable<any>{
    return this.http.get<any>(
      //`http://localhost:8090/oauth2/authorize/github`,
      AppConstants.GITHUB_AUTH_URL,
      httpOptions);

  }

  connectPage() {
    return this.http.get<any>(
      `http://localhost:8090/login`,
      httpOptions);
  }
}
