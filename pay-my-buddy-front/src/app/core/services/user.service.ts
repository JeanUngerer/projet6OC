import {Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiService} from "./api.service";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {distinctUntilChanged} from "rxjs/operators";
import {AddFundsDTO, MyBalanceDTO, MyTransfersDTO} from "../models/transfer.model";
import {environment} from "../../../environments/environment";
import {LoginWatcher} from "./loginWatcher.service";
import {MatTableDataSource} from "@angular/material/table";
import {MyContact} from "../models/contact.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private  apiService: ApiService, private loginWatcher: LoginWatcher) { }

  private myBalance: Number = 0;

  latestBalanceUpdateSubject: BehaviorSubject<number> =
    new BehaviorSubject(0);



  private generalDataLoadingSubject = new BehaviorSubject<number>(0);
  public isLoadingGeneralData = this.generalDataLoadingSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  isUser(): Observable<any> {
    return this.apiService.get('/authi');
  }

  handleBalanceUpdate() {
    if (this.loginWatcher.logoutIfInvalidToken()) {
      this.setMyBalance(0);
      return;
    }
    this.getBalance()
      .subscribe({
        next: res => {
          this.generalDataLoadingSubject.next(res.balance);
          console.log("GET_BALANCE : ");
        },
        error: err => {console.log("ERROR : ", err)}
      })
  }

  getBalance(): Observable<MyBalanceDTO> {
    return this.apiService.get(`/${environment.apiUser}/mybalance`);
  }

  addFunds(addFunds: AddFundsDTO): Observable<MyBalanceDTO> {
    return this.apiService.put(`/${environment.apiUser}/addfunds`, addFunds);
  }

  getMyBalance(){
    return this.myBalance;
  }

  setMyBalance(balance: Number){
    this.myBalance = balance;
  }

}
