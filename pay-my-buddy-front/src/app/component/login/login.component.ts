import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoginService} from "../../core/services/login.service";
import {MatCheckboxModule} from '@angular/material/checkbox';
import {AppConstants} from "../../../environments/app.constants";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  emailControl = new FormControl('', [Validators.required]);
  passwordControl = new FormControl('', Validators.required);
  submitted: boolean = false;
  loginForm = new FormGroup({
    email: this.emailControl,
    password: this.passwordControl,
  });

  checkRemember: boolean = false;

  loginPage: any = null;

  displayLoginPage: boolean = false;
  hasAuthError: boolean = false;

  googleURL = AppConstants.GOOGLE_AUTH_URL;
  githubURL = AppConstants.GITHUB_AUTH_URL;


  constructor(
    private router: Router,
    private authService: AuthService,
    private loginService: LoginService,
    private route: ActivatedRoute,
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
      }).subscribe({
        next:(r) => {console.log("RESULT : ", r); this.handleAuthSuccess()},
        error:(err) => this.handleAuthError(err)}
      );
    }
  }

  /**
   * If login and password match, access to the site
   */
  handleAuthSuccess() {
    this.hasAuthError = false;
    this.router.navigate(['home']);
  }

  /**
   * else display error to the user
   * @param err
   */
  handleAuthError(err: any) {
    this.hasAuthError = true;
    console.log("LOG ERROR : ", err);
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

  register() {
    this.router.navigate(['register']);
  }

  logGithub(){
    const res = this.loginService.connectGithub() /*.subscribe({
      next: (res) => console.log("GITHUB RES : ", res),
      error: (err) => console.log("GITHUB ERROR : ", err)
    })*/;
    console.log("RES : ", res);
  }

  logGoogle(){
    const res = this.loginService.connectGoogle() /*.subscribe({
      next: (res) => console.log("GITHUB RES : ", res),
      error: (err) => console.log("GITHUB ERROR : ", err)
    })*/;
    console.log("RES : ", res);
  }

  goToHome(){
    this.router.navigate(['home']);
  }

}
