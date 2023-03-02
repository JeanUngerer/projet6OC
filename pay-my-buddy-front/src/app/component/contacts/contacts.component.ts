import {Component, OnInit, ViewChild} from '@angular/core';
import {ContactService} from "../../core/services/contact.service";
import {MyContact} from "../../core/models/contact.model";
import {Transfer} from "../../core/models/transfer.model";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {TransferService} from "../../core/services/transfer.service";
import {identity} from "rxjs";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent implements OnInit{

  contactList:MyContact[] = [];


  readyToDisplay = false;
  submitted = false;
  displayedColumns: string[] = ['firstname', 'lastname', 'username', 'email'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  dataSource = new MatTableDataSource<MyContact>(this.contactList);

  contactName = new FormControl(null, [Validators.required, Validators.minLength(1)]);
  contactMail = new FormControl(null, [Validators.email, Validators.required]);
  newContactForm = new FormGroup({
    contactName: this.contactName,
    contactMail: this.contactMail,
  });
  constructor(

    private contactService: ContactService,
  ) {}
  ngOnInit(): void {
    //this.addContactByUsername();
    this.refreshContactList();

    this.readyToDisplay = true;
  }

  refreshContactList(){
    this.contactService.myContacts()
      .subscribe({
        next: res => {
            this.contactList = res.myContacts;
            console.log("CONTACT LIST", res);
            this.dataSource = new MatTableDataSource<MyContact>(this.contactList);
            this.dataSource.paginator = this.paginator;
            console.log("DATASOURCE : ", this.dataSource);},
        error: err => {console.log("ERROR : ", err)}
      })
    return true;
  }

  addContactByUsername(){
    this.submitted = true;
    let identifier = "";
    if(!this.newContactForm.controls.contactName.valid)
    {
      console.log("BAD REQUEST : username not valid");
      return;
    }
    // @ts-ignore
    identifier = this.newContactForm.controls.contactName.value;

    this.contactService.addContactByUsername(identifier)
      .subscribe({
      next: res => {
        this.contactList = res.myContacts,
        console.log("ADD Contact username : ", res);
        this.dataSource = new MatTableDataSource<MyContact>(this.contactList);
        this.dataSource.paginator = this.paginator;
        console.log("DATASOURCE : ", this.dataSource);},
      error: err => {console.log("ERROR : ", err)}
    })
  }

  hasError(field: string) {
    return !this.newContactForm.get(field)?.valid;

  }

  resetSubmitted() {
    this.submitted= false;
  }

}
