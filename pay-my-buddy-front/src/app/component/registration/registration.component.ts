import { Component } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../core/services/auth.service";
import {LoginService} from "../../core/services/login.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {



  usernameControl = new FormControl('', Validators.required);
  emailControl = new FormControl('', [Validators.required, Validators.email]);
  passwordControl = new FormControl('', Validators.required);
  passwordMatchControl = new FormControl('', [Validators.required]);
  firstnameControl = new FormControl('', Validators.required);
  lastnameControl = new FormControl('', Validators.required);
  submitted: boolean = false;
  registerForm = new FormGroup({
    username: this.usernameControl,
    email: this.emailControl,
    password: this.passwordControl,
    passwordMatch: this.passwordMatchControl,
    firstname: this.firstnameControl,
    lastname:this.lastnameControl
  });
  hasRegisterError:boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  register(){
    this.submitted = true;
    if (this.isValid()) {
      this.authService.register({
        username: this.username ? this.username : '',
        email: this.email ? this.email : '',
        password: this.password ? this.password : '',
        firstname: this.firstname ? this.firstname : '',
        lastname: this.lastname ? this.lastname : '',
      }).subscribe({
        next:(r) => {console.log("RESULT : ", r); this.handleAuthSuccess()},
        error:(err) => this.handleAuthError(err)}
      );
    }
  }

  isValid() {
    return this.registerForm.valid && this.matches(this.registerForm);
  }


  handleAuthSuccess() {
    this.hasRegisterError = false;
    this.router.navigate(['login']);
  }

  handleAuthError(err: any) {
    this.hasRegisterError = true;
    console.log("LOG ERROR : ", err);
  }

  goToHome(){
    this.router.navigate(['home']);
  }

  hasError(field: string) {
    const has =
      this.submitted && !this.registerForm.get(field)?.valid ? true : false;
    if(field === "passwordMatch"){
      return  has && !this.matches(this.registerForm);
    }
    return has;
  }

  handleChange() {
    this.hasRegisterError = false;
  }

  matches(form: AbstractControl){
    return form.get('password')?.value === form.get('passwordMatch')?.value;
    console.log('P1 : ', form.get('password')?.value, '  P2 : ', form.get('passwordMatch')?.value)
  }

  get email() {
    return this.registerForm.get('email')?.value;
  }
  get password() {
    return this.registerForm.get('password')?.value;
  }

  get username() {
    return this.registerForm.get('username')?.value;
  }

  get firstname() {
    return this.registerForm.get('firstname')?.value;
  }

  get lastname() {
    return this.registerForm.get('lastname')?.value;
  }



}
