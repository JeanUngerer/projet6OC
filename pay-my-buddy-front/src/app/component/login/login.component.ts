import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  emailControl = new FormControl('', [Validators.required, Validators.email]);
  passwordControl = new FormControl('', Validators.required);
  submitted: boolean = false;
  loginForm = new FormGroup({
    email: this.emailControl,
    password: this.passwordControl,
  });
  hasAuthError: boolean = false;
  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}
  ngOnInit(): void {

  }

  /**
   * Method triggered on form submit. Check the info and return according to api value.
   */
  login() {
    this.submitted = true;
    if (this.isValid()) {
      this.authService.login({
        email: this.email ? this.email : '',
        password: this.password ? this.password : '',
      }).subscribe(
        (r) => this.handleAuthSuccess(),
        (err) => this.handleAuthError(err)
      );
    }
  }

  /**
   * If login and password match, access to the site
   */
  handleAuthSuccess() {
    this.hasAuthError = false;
    this.router.navigate(['/geo/home']);
  }

  /**
   * else display error to the user
   * @param err
   */
  handleAuthError(err: any) {
    this.hasAuthError = true;
  }

  hasError(field: string) {
    const has =
      this.submitted && !this.loginForm.get(field)?.valid ? true : false;
    return has;
  }

  isValid() {
    return !this.hasError('email') && !this.hasError('password');
  }

  handleChange() {
    this.hasAuthError = false;
  }

  get email() {
    return this.loginForm.get('email')?.value;
  }
  get password() {
    return this.loginForm.get('password')?.value;
  }
}
