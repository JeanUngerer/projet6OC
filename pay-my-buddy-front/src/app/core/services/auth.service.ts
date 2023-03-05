import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, ReplaySubject} from 'rxjs';
import { distinctUntilChanged, map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthUser } from '../models/auth.model';
import { JWTService } from './jwt.service';
import {ApiService} from "./api.service";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<AuthUser>({} as AuthUser);
  public currentUser = this.currentUserSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  private isAuthenticatedSubject = new ReplaySubject<boolean>(1);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient,
              private jwt: JWTService,
              private apiService: ApiService) {
    this.isLoggedIn();
  }

  login({ email, password }: { email: string; password: string }) {

    return this.http
      .post<any>(`${environment.apiUrl}/token`, null,
        {
          headers: new HttpHeaders(
            {
              'Authorization': "Basic " + btoa(email + ":" + password)
            } ),
          observe: 'response'
        })
      .pipe(tap((r) => {console.log("TOKENNN : ", r.headers.get('Token')),
          this.createSession(r.headers.get('Token'))
      }));
  }

  register ({username, email, password, firstname, lastname}: {username : string, email : string, password : string, firstname : string, lastname : string}){
    return this.apiService.putNoAuth(`/register`,
{
        username : username,
        mail : email,
        password : password,
        firstname : firstname,
        lastname : lastname
    })
  }

  /**
   * deletes JWT Token
   * Resets isAuthenticated and CurrentUser observables
   */
  logout() {
    this.jwt.deleteToken();
    this.isAuthenticatedSubject.next(false);
    this.currentUserSubject.next({});
  }

  /**
   * saves User token and updates observables currentUserSubject and isAuthenticatedSubject
   s  */
  createSession(token: string | null) {
    if (token == null){return;}
    const authUser: AuthUser | null = this.jwt.saveUser(token);
    if (authUser) {
      this.currentUserSubject.next(authUser);
      this.isAuthenticatedSubject.next(true);
    }
  }

  /**
   *
   * @returns Observable<string | undefined> coresponding to current user Role
   */
  getRole() {
    return this.currentUser.pipe(
      map((u) => {
        return u.role || this.jwt.getUserFromToken()?.role;
      })
    );
  }

  /**
   *
   * @returns Observable<string | undefined> coresponding to current user username
   */
  getUsername() {
    return this.currentUser.pipe(
      map((u) => {
        return u.username || this.jwt.getUserFromToken()?.username;
      })
    );
  }

  /**
   *
   * @returns a boolean to indicate if a token that has not expired is saved in localStorage
   * Real validity check on the token is done server-side
   */
  isLoggedIn() {
    return this.jwt.hasValidToken();
  }
}
