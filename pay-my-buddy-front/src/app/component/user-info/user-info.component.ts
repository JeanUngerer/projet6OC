import {Component, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../core/services/user.service";
import {Transfer} from "../../core/models/transfer.model";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MyContact} from "../../core/models/contact.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit{

  submitted = false;
  amountControl = new FormControl(0, [Validators.min(0), Validators.required]);
  addMoneyForm = new FormGroup({
    amount: this.amountControl,
  });

  withdrawMoneyForm = new FormGroup({
    amount: this.amountControl,
  });


  constructor(
    private userService: UserService,
  ) {}



  ngOnInit(): void {
    this.userService.handleBalanceUpdate();
  }

  hasError(field: string) {
    return !this.addMoneyForm.get(field)?.valid && this.submitted;

  }

  resetSubmitted() {
    this.submitted= false;
  }

  sendFunds(){

    this.submitted = true;

    if(!this.addMoneyForm.valid){return;}

    this.userService.addFunds({
      amount: this.addAmount
    }).subscribe({
      next:(r) => {
        console.log("RESULT : ", r);

        this.submitted = false;
        this.userService.handleBalanceUpdate();},
      error:(err) => console.log("ERROR : ", err)}
    );
  }

  withdrawFunds(){

    this.submitted = true;

    if(!this.withdrawMoneyForm.valid){return;}

    this.userService.withdrawFunds({
      amount: this.withdrawAmount
    }).subscribe({
      next:(r) => {
        console.log("RESULT : ", r);

        this.submitted = false;
        this.userService.handleBalanceUpdate();},
      error:(err) => console.log("ERROR : ", err)}
    );
  }

  get addAmount() {
    const amount = this.addMoneyForm.get('amount')?.value;
    if (amount){
      return amount;
    }
    return 0;
  }

  get withdrawAmount() {
    const amount = this.withdrawMoneyForm.get('amount')?.value;
    if (amount){
      return amount;
    }
    return 0;
  }


}
