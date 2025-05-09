import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import {UserService} from "../services/user.service";

@Injectable({
  providedIn: 'root'
})

export class ReadGuard implements CanActivate {

  constructor(private userService: UserService) {

  }

  canActivate(): boolean {
    let user = this.userService.getCurrentUser()
    if(this.userService.hasPermission(user, "can_read_users"))
      return true
    else {
      alert("User does not have READ permission!")
      return false
    }
  }

}
