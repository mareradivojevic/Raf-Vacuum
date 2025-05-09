export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  jwt: string
}

export interface Permission {
  id : number
  name : string
}

export interface Vacuum {
  id: number
  name: string
  status: string
  dateCreated: number
  active: boolean
}

export interface User {
  id : number
  email: string
  password: string
  name: string
  surname: string
  permissions: Permission[]
}

