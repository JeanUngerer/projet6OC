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

  constructor(

    private authService: AuthService,
    private userService: UserService,
  ) {}

  latestBalanceUpdate$ = this.userService.latestBalanceUpdateSubject
    .asObservable()

  myBalance : Number = 0;
  ngOnInit(): void {
    this.myBalance = this.userService.getMyBalance();

    this.userService.isLoadingGeneralData.subscribe({
      next: (v) => {this.myBalance = v;
        console.log("UPDATE BALANCE : ", v)}
    })
  }
  handleClickLogout() {
    this.authService.logout();
  }

  handleBalanceChange(){
    return this.latestBalanceUpdate$.pipe(r => {this.myBalance = this.userService.getMyBalance(); return of(null)})
  }





}
