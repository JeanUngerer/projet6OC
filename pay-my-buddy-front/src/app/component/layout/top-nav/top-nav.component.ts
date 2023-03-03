import { Component, OnInit } from '@angular/core';
import { AuthService} from "../../../core/services/auth.service";
import {UserService} from "../../../core/services/user.service";
import {of} from "rxjs";
import {distinctUntilChanged} from "rxjs/operators";

@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.scss']
})
export class TopNavComponent implements OnInit{


  logged : boolean = false;
  constructor(

    private authService: AuthService,
    private userService: UserService,
  ) {}

  latestBalanceUpdate$ = this.userService.latestBalanceUpdateSubject
    .asObservable()

  myBalance : string = '0';

  myIdentifier? : string = "";
  ngOnInit(): void {

    this.logged = this.authService.isLoggedIn();

    this.myBalance = this.userService.getMyBalance().toString();

    this.authService.getUsername().subscribe({
      next: (v) => {this.myIdentifier = v;
        this.logged = this.authService.isLoggedIn();
        console.log("UPDATE username : ", v)}
    })

    this.userService.isLoadingGeneralData.subscribe({
      next: (v) => {this.myBalance = v.toString();
        this.logged = this.authService.isLoggedIn();
        console.log("UPDATE BALANCE : ", v)}
    })
  }
  handleClickLogout() {
    this.authService.logout();
  }

  handleBalanceChange(){
    return this.latestBalanceUpdate$.pipe(r => {this.myBalance = this.userService.getMyBalance().toString(); return of(null)})
  }





}
