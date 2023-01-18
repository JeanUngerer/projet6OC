import {MyContact} from "./contact.model";

export interface Transfer {
  amount: Number;
  receiverUsername: String;
  date: Date;
  description: String;
}


export interface TransferToSend{
  sendTo: MyContact;
  amount: Number;
}
