import {MyContact} from "./contact.model";

export interface Transfer {
  amount: Number;
  receiverUsername: String;
  date: Date;
  description: String;
}

export interface MyTransfersDTO{
  myTransactionList: Transfer[];
}


export interface TransferToSend{
  sendTo: MyContact;
  amount: Number;
}

export interface MyBalanceDTO{
  balance: number;
}

export interface AddFundsDTO {
  amount: number;
}
