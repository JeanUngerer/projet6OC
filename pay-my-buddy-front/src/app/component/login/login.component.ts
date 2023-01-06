import { Component, OnInit } from '@angular/core';
import { LoginService} from "../../core/services/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{

  constructor(private loginService: LoginService) {
  }

  ngOnInit(): void {

    this.loginGithub()
  }

  loginGithub(){
    console.log(this.loginService.connectGithub().subscribe());

  }
}
