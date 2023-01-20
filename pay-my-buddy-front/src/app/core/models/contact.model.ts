export interface MyContact {
  mail: string;
  firstname: string;
  lastname: string;
  username: string;
}

export interface MyContactsDTO {
  myContacts: MyContact[];
}
