import {Component, OnInit} from '@angular/core';
import {User} from "../model";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {

  user: User
  create: boolean
  read: boolean
  update: boolean
  delete: boolean
  add: boolean
  search: boolean
  remove: boolean
  start: boolean
  stop: boolean
  discharge: boolean

  constructor(private userService: UserService) {
    this.user = this.userService.getEditUser()
    this.create = this.hasPermission(this.user, "can_create_users")
    this.read = this.hasPermission(this.user, "can_read_users")
    this.update = this.hasPermission(this.user, "can_update_users")
    this.delete = this.hasPermission(this.user, "can_delete_users")
    this.add = this.hasPermission(this.user, "can_add_vacuum")
    this.search = this.hasPermission(this.user, "can_search_vacuum")
    this.remove = this.hasPermission(this.user, "can_remove_vacuum")
    this.start = this.hasPermission(this.user, "can_start_vacuum")
    this.stop = this.hasPermission(this.user, "can_stop_vacuum")
    this.discharge = this.hasPermission(this.user, "can_discharge_vacuum")
  }

  ngOnInit(): void {

  }

  public editUser() {
    //PUT http://localhost:8080/api/users
    this.userService.editUser(this.user, this.setPermissions()).subscribe((user) => {
      alert("User with id: "+user.id+" updated!")
      this.user = user

      let currentUser = this.userService.getCurrentUser()
      if(currentUser.id == user.id)
        this.userService.updateCurrentUser(user)
    })
  }

  public hasPermission(user: User, permission: string): boolean {
    return this.userService.hasPermission(user, permission)
  }

  public setPermissions() : string[] {
    const permissions: string[] = [];
    if(this.create) permissions.push("can_create_users")
    if(this.read) permissions.push("can_read_users")
    if(this.update) permissions.push("can_update_users")
    if(this.delete) permissions.push("can_delete_users")
    if(this.add) permissions.push("can_add_vacuum")
    if(this.search) permissions.push("can_search_vacuum")
    if(this.remove) permissions.push("can_remove_vacuum")
    if(this.start) permissions.push("can_start_vacuum")
    if(this.stop) permissions.push("can_stop_vacuum")
    if(this.discharge) permissions.push("can_discharge_vacuum")

    return permissions
  }

}
