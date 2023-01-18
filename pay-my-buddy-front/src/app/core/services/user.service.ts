import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiService} from "./api.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private  apiService: ApiService) { }

  isUser(): Observable<any> {
    return this.apiService.getNoAuth('/authi');
  }

}
