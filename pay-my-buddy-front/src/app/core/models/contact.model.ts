export interface MyContact {
  mail: string;
  firstname: string;
  lastname: string;
  username: string;
  identifier: string;
}

export interface MyContactsDTO {
  myContacts: MyContact[];
}
