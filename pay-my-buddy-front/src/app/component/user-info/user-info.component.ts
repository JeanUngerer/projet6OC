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

  modificationControl = new FormControl(null);
  editPhoneNumberForm = new FormGroup({
    modif: this.modificationControl,
  });



  editFnameForm = new FormGroup({
    modif: this.modificationControl,
  });

  editLnameForm = new FormGroup({
    modif: this.modificationControl,
  });

  currentPhone? = "";
  currentFname? = "";
  currentLname? = "";

  constructor(
    private userService: UserService,
  ) {}



  ngOnInit(): void {
    this.userService.handleBalanceUpdate();
    this.userService.getPhoneNumber().subscribe( {
      next: (v) => {v.modification? this.currentPhone = v.modification : this.currentPhone = 'Not Specified';
      console.log("CURRENT PHONE : ", v.modification)}
    })
    this.userService.getFirstname().subscribe({
      next: (v) => {v.modification? this.currentFname = v.modification : this.currentFname = 'Not Specified'}
    })
    this.userService.getLastname().subscribe({
      next: (v) => {v.modification? this.currentLname = v.modification : this.currentLname = 'Not Specified'}
    })
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

  modifPhone(){
    if(!this.editPhoneNumberForm.valid){return;}

    this.userService.setPhoneNumber({modification:this.newPhone}).subscribe({
      next: (v) => {this.currentPhone = v.modification}
    });
  }

  modifFname(){
    if(!this.editPhoneNumberForm.valid){return;}

    this.userService.setFirstname({modification:this.newFname}).subscribe({
      next: (v) => {this.currentFname = v.modification}
    });
  }

  modifLname(){
    if(!this.editPhoneNumberForm.valid){return;}

    this.userService.setLastname({modification:this.newLname}).subscribe({
      next: (v) => {this.currentLname = v.modification}
    });
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

  get newPhone(){
    const newValue = this.editPhoneNumberForm.controls.modif.value;
    if (newValue){
      return newValue;
    }
    return undefined;
  }

  get newFname(){
    const newValue = this.editFnameForm.controls.modif.value;
    if (newValue){
      return newValue;
    }
    return undefined;
  }

  get newLname(){
    const newValue = this.editLnameForm.controls.modif.value;
    if (newValue){
      return newValue;
    }
    return undefined;
  }


}
