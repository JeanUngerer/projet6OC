import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

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
      `http://localhost:8090/login/oauth2/code/github`,
      httpOptions);

  }

  connectPage() {
    return this.http.get<any>(
      `http://localhost:8090/login`,
      httpOptions);
  }
}
