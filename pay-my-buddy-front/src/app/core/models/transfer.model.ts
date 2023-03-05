import {MyContact} from "./contact.model";

export interface Transfer {
  amount: Number;
  senderUsername: String;
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
  description: String;
}

export interface MyBalanceDTO{
  balance: number;
}

export interface AddFundsDTO {
  amount: number;
}
