import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import {UserService} from "../services/user.service";

@Injectable({
  providedIn: 'root'
})

export class SearchGuard implements CanActivate {

  constructor(private userService: UserService) {

  }

  canActivate(): boolean {
    let user = this.userService.getCurrentUser()
    if(this.userService.hasPermission(user, "can_search_vacuum"))
      return true
    else {
      alert("User does not have SEARCH permission!")
      return false
    }
  }

}

