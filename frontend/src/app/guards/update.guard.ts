import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import {UserService} from "../services/user.service";

@Injectable({
  providedIn: 'root'
})

export class UpdateGuard implements CanActivate {

  constructor(private userService: UserService) {

  }

  canActivate(): boolean {
    let user = this.userService.getCurrentUser()
    if(this.userService.hasPermission(user, "can_update_users"))
      return true
    else {
      alert("User does not have UPDATE permission!")
      return false
    }
  }

}
