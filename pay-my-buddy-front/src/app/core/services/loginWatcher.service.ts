import {Injectable} from '@angular/core';
import {JWTService} from './jwt.service';
import {Router} from '@angular/router';
import {AuthService} from './auth.service';



@Injectable({
  providedIn: 'root',
})
export class LoginWatcher {
  constructor(
    private jwtService: JWTService,private router: Router,private authService: AuthService
  ) {
  }

  /**
   * Check if the JWT is not expired.
   * Used before a call to an api
   */
  logoutIfInvalidToken(): boolean {
    if (!this.jwtService.hasValidToken()) {
      this.authService.logout();
      this.router.navigate([`/login`]);
      return true;
    }
    return false;
  }
}
