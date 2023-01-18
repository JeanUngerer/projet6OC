import {Component, OnInit} from '@angular/core';
import {ContactService} from "../../core/services/contact.service";
import {MyContact} from "../../core/models/contact.model";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent implements OnInit{

  contactList:MyContact[] = [];
  constructor(

    private contactService: ContactService,
  ) {}
  ngOnInit(): void {
    this.addContactByUsername();
    //this.refreshContactList();
  }

  refreshContactList(){
    this.contactService.myContacts()
      .subscribe({
        next: res => {this.contactList = res, console.log("CONTACT LIST", res)},
        error: err => {console.log("ERROR : ", err)}
      })
  }

  addContactByUsername(){
    this.contactService.addContactByUsername("usernameAdmin")      .subscribe({
      next: res => {this.contactList = res, console.log("ADD Contact username : ", res)},
      error: err => {console.log("ERROR : ", err)}
    })
  }

}
