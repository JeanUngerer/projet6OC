import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {MyContact, MyContactsDTO} from "../models/contact.model";

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  constructor(private  apiService: ApiService) { }

  myContacts(): Observable<MyContactsDTO> {
    return this.apiService.get(`/${environment.apiContact}/mycontacts`);
  }

  addContactByUsername(username: String): Observable<MyContactsDTO> {
    return  this.apiService.put(`/${environment.apiContact}/addcontactbyusername`,
      {
        username: username
      })
  }

}
