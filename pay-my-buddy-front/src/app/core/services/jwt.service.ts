import { Injectable } from '@angular/core';
import { AuthUser } from '../models/auth.model';

@Injectable({
  providedIn: 'root',
})
export class JWTService {
  constructor() {}
  token_key = 'pay-my-buddy_token_key';

  decodeToken(token: string) {
    return JSON.parse(atob(token.split('.')[1]));
  }

  saveToken(token: string) {
    localStorage.setItem(this.token_key, token);
  }

  getToken() {
    return localStorage.getItem(this.token_key);
  }

  deleteToken() {
    return localStorage.removeItem(this.token_key);
  }

  getBearerToken() {
    const token = localStorage.getItem(this.token_key);
    return token ? `Bearer ${token}` : undefined;
  }

  saveUser(token: string): AuthUser | null {
    if (token) {
      this.saveToken(token);
      const decoded = this.decodeToken(token);
      return this.getUserFromToken();
    }
    throw Error('no token at createSession');
  }

  getUserFromToken(): AuthUser | null {
    const token = this.getToken();
    if (token) {
      const decoded = this.decodeToken(token);
      console.log("Decode : ", decoded );
      return {
        role: decoded.scope as string,
        username: decoded.sub as string,
        exp: decoded.exp,
      };
    }
    return null;
  }

  hasValidToken() {
    const authUser = this.getUserFromToken();
    if (authUser && authUser.role && authUser.username && authUser.exp) {
      const isValid = Date.now() <= authUser.exp * 1000;
      return isValid;
    }
    return false;
  }
}
