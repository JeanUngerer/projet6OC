import {Component, OnInit} from '@angular/core';
import { UserService} from "../../core/services/user.service";
import {Router} from "@angular/router";
import {AuthService} from "../../core/services/auth.service";
import {LoginService} from "../../core/services/login.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  welcomeText:any = null;
  constructor(

    private userService: UserService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.userService.isUser().subscribe({next: res => {this.welcomeText = res.message; console.log("RES : ", this.welcomeText);}});

    if (this.authService.isLoggedIn()){
      this.userService.handleBalanceUpdate();
    }
    else {
      this.userService.setMyBalance(0);
    }

  }




}
