import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {LoginRequest, LoginResponse} from "../model";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private readonly apiUrl = environment.login;

  private loginError: boolean
  private loggedInUser: string

  constructor(private httpClient: HttpClient) {
    this.loginError = false
    this.loggedInUser = ""
  }

  public getLoginError(): boolean {
    return this.loginError
  }

  public setLoginError(loginError: boolean) {
    this.loginError = loginError
  }

  public getLoggedInUser(): string {
    return this.loggedInUser
  }

  public setLoggedInUser(loggedInUser: string) {
    this.loggedInUser = loggedInUser
  }

  public login(loginRequest: LoginRequest) : Observable<LoginResponse> {

    return this.httpClient.post<LoginResponse>(this.apiUrl, {
      username: loginRequest.username,
      password: loginRequest.password
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      alert("An error with status code "+error.status+" occurred: "+error.error);
    } else {
      if(error.status == 401)
        alert(error.error);
    }

    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

}
