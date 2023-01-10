import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, ReplaySubject} from 'rxjs';
import { distinctUntilChanged, map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthUser } from '../models/auth.model';
import { JWTService } from './jwt.service';

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

  constructor(private http: HttpClient, private jwt: JWTService) {
    this.isLoggedIn();
  }

  login({ email, password }: { email: string; password: string }) {
    return this.http
      .post<{ Token: string }>(`${environment.apiUrl}/token`, {
        username: email,
        password,
      })
      .pipe(tap((r) => this.createSession(r.Token)));
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
  createSession(token: string) {
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
   * @returns a boolean to indicate if a token that has not expired is saved in localStorage
   * Real validity check on the token is done server-side
   */
  isLoggedIn() {
    return this.jwt.hasValidToken();
  }
}
