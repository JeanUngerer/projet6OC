export interface AuthUser {
  role?: string;
  username?: string;
  exp?: number;
}


export interface User {
  email: string;
  firstName: string;
  lastName: string;
  role: string;
}

export interface LoginUser {
  username: string;
  password: string;
}

export interface ProfileModificationDTO {
  modification?: string;
}
