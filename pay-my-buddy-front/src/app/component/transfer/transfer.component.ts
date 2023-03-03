import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {Transfer, TransferToSend} from "../../core/models/transfer.model";
import {TransferService} from "../../core/services/transfer.service";
import {MyContact} from "../../core/models/contact.model";
import {ContactService} from "../../core/services/contact.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../core/services/user.service";
import {AuthService} from "../../core/services/auth.service";

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnInit{

  submitted = false;
  transfers: Transfer[] = [];
  displayedColumns: string[] = ['sentTo','sentFrom', 'amount', 'fee', 'date', 'description'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  dataSource = new MatTableDataSource<Transfer>(this.transfers);

  contactList:MyContact[] = [];

  usernameList:String[] = [];

  myUsername = '';

  sentResponse:String = "";

  contactControl = new FormControl(null, [Validators.required]);
  amountControl = new FormControl(0, [Validators.min(0), Validators.required]);

  descriptionControl = new FormControl(null, Validators.maxLength(255));

  tranferForm = new FormGroup({
    contact: this.contactControl,
    amount: this.amountControl,
    description: this.descriptionControl,
  });

  constructor(

    private transferService: TransferService,
    private contactService: ContactService,
    private userService: UserService,

    private authService: AuthService,
  ) {}

  ngOnInit(): void {
      this.refreshTransfersList();
      this.refreshContactList();
      this.userService.handleBalanceUpdate();

      this.authService.getUsername().subscribe({next: name => name? this.myUsername = name : this.myUsername = "notAuthenticated"});
  }


  refreshContactList(){
    this.contactService.myContacts()
      .subscribe({
        next: res => {this.updateContactList(res.myContacts), console.log("CONTACT LIST", res)},
        error: err => {console.log("ERROR : ", err)}
      })
  }

  refreshTransfersList(){
    this.transferService.myTransfers().subscribe({
      next:(r) => {
        console.log("RESULT : ", r.myTransactionList);
        this.transfers = r.myTransactionList;
        this.dataSource = new MatTableDataSource<Transfer>(this.transfers);
        },
      error:(err) => console.log("ERROR : ", err)}
    );
  }

  sendMoney(){

    this.submitted = true;
    let friendToSendTo = this.transferTo;

    if(friendToSendTo === ''){return;}

    this.transferService.sendNewTransfer({
      sendTo: this.contactList.filter(contact => contact.username === friendToSendTo)[0],
      amount: this.transferAmount,
      description: this.transferDescription,
    }).subscribe({
      next:(r) => {
        console.log("RESULT : ", r);
        this.refreshTransfersList();
        this.submitted = false;
        this.sentResponse = r.message;
        this.userService.handleBalanceUpdate();},
      error:(err) => console.log("ERROR : ", err)}
    );
  }


  get transferTo() {
    const contact = this.tranferForm.controls.contact.value;
    if (contact){
      return contact;
    }
    return '';
  }
  get transferAmount() {
    const amount = this.tranferForm.get('amount')?.value;
    if (amount){
      console.log("AMMMOUUNNNTT : ", amount);
      return amount;
    }
    return 0;
  }

  get transferDescription(){
    return this.tranferForm.controls.description.value? this.tranferForm.controls.description.value : "";
  }

  hasError(field: string) {
    return !this.tranferForm.get(field)?.valid && this.submitted;

  }

  resetSubmitted() {
    this.submitted= false;
    this.sentResponse = "";
    console.log("Username value : ", this.tranferForm.controls.contact.value);
  }

  updateContactList(liste: MyContact[]){
    this.contactList = liste;
    this.usernameList = Array.from(liste, elem => elem.username);
    console.log("UsernameListe : ", this.usernameList);
  }
}
