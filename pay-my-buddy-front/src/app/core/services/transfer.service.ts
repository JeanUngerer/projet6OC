import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {Transfer, TransferToSend} from "../models/transfer.model";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TransferService {

  constructor(private  apiService: ApiService) { }

  myTransfers(): Observable<Transfer[]> {
    return this.apiService.get(`/${environment.apiTransfer}/mytransactions`);
  }

  sendNewTransfer(newTransfer: TransferToSend){
    return this.apiService.put(`/${environment.apiTransfer}/sendmoney`, newTransfer);
  }

}
