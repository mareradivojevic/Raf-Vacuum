import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Permission, User} from "../model";
import {catchError, Observable, throwError} from "rxjs";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = environment.users
  private helper: JwtHelperService = new JwtHelperService()
  private httpOptions = {headers: new HttpHeaders()}


  private currentUser: User = {
    id: 0,
    email: "",
    password: "",
    name: "",
    surname: "",
    permissions: []
  }

  private userToEdit: User = {
    id: 0,
    email: "",
    password: "",
    name: "",
    surname: "",
    permissions: []
  }

  constructor(private httpClient: HttpClient) {}

  public getCurrentUser(): User {
    return this.currentUser
  }

  public setCurrentUser(jwt: string) {
    let decodedJwt = this.helper.decodeToken(jwt)
    this.currentUser.id = decodedJwt.id
    this.currentUser.email = decodedJwt.sub
    this.currentUser.permissions = decodedJwt.permissions

  }

  public updateCurrentUser(user: User) {
    this.currentUser = user
  }

  public getEditUser(): User {
    return this.userToEdit
  }

  public setEditUser(user: User) {
    this.userToEdit = user
  }

  public removeCurrentUser() {
    this.currentUser.id = 0
    this.currentUser.email = "",
    this.currentUser.password = "",
    this.currentUser.name = "",
    this.currentUser.surname = "",
    this.currentUser.permissions = []
  }

  public getUsers(): Observable<User[]> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.get<User[]>(this.apiUrl, this.httpOptions)
  }

  public hasPermission(user: User, permission: string) : boolean {
    for(let i = 0; i < user.permissions.length; i++)
      if(user.permissions[i].name === permission)
        return true

    return false
  }

  public createUser(name: string, surname: string, email: string, password: string, permissions: string[]) : Observable<User> {

    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.post<User>(this.apiUrl, {
      email: email,
      password: password,
      name: name,
      surname: surname,
      permissions: permissions
    }, this.httpOptions)
      .pipe(
      catchError(this.handleError)
    );

  }

  public editUser(user: User, permissions: string[]): Observable<User> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.put<User>(this.apiUrl, {
      id: user.id,
      email: user.email,
      password: user.password,
      name: user.name,
      surname: user.surname,
      permissions: permissions
    }, this.httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  // public updateCurrentUser(): Observable<User> {
  //   let jwt = localStorage.getItem("jwt")
  //   if(jwt != null)
  //     this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)
  //
  //
  //   return this.httpClient.get<User>(this.apiUrl+'/'+this.currentUser.id, this.httpOptions).pipe(
  //     catchError(this.handleError)
  //   );
  // }

  public delete(user: User): Observable<User> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.delete<User>(this.apiUrl+'/'+user.id, this.httpOptions).pipe(
      catchError(this.handleError)
    );

  }

  private handleError(error: HttpErrorResponse) {
    // Extract the error message from the response body
    let errorMessage = 'An unknown error occurred!';
    if(error.status == 403)
      errorMessage = 'User does not have DELETE permission!'
    if(error.status == 404)
      errorMessage = 'User not found'

    return throwError(() => new Error(errorMessage));
  }

}
