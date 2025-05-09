import {Component, OnInit} from '@angular/core';
import {LoginService} from "../services/login.service";
import {LoginRequest, LoginResponse, User} from "../model";
import {UserService} from "../services/user.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginRequest: LoginRequest = {
    username: "",
    password: ""
  }

  loginResponse: LoginResponse = {
    jwt: "",
  };

  constructor(private loginService: LoginService, private userService: UserService) {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.loginResponse.jwt = jwt

  }

  public getLoggedInUser(): string {
    return this.loginService.getLoggedInUser()
  }

  public getLoginError(): boolean {
    return this.loginService.getLoginError()
  }

  public login() {
    this.loginService.login(this.loginRequest).subscribe(
      (loginResponse) => {
        alert("Successful login!")
        this.loginResponse = loginResponse
        this.loginService.setLoginError(false)

        this.userService.setCurrentUser(loginResponse.jwt)
        this.loginService.setLoggedInUser(this.loginRequest.username)
        localStorage.setItem("jwt", loginResponse.jwt)
      }, (error) => {
        this.loginService.setLoginError(true)
      }
    )
  }

  public logout() {
    this.userService.removeCurrentUser()
    this.loginService.setLoggedInUser("")
    this.loginResponse.jwt = ""

    localStorage.removeItem("jwt")
  }



}
