export interface AuthUser {
  role?: string;
  email?: string;
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
