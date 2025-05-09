import {Component, OnInit} from '@angular/core';
import {User} from "../model";
import {UserService} from "../services/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[];

  constructor(private userService: UserService) {
    this.users = []
  }

  ngOnInit(): void {
    this.getUsers()
  }

  public getCurrentUser(): User {
    return this.userService.getCurrentUser()
  }

  public getEditUser(): User {
    return this.userService.getEditUser()
  }

  public setEditUser(user: User) {
    this.userService.setEditUser(user)
  }

  public getUsers(): void {
    //GET http://localhost:8080/api/users
    this.userService.getUsers().subscribe(users => {
      this.users = users
    });
  }

  public deleteUser(user: User): void {
    this.userService.delete(user).subscribe({
      next: () => {
        alert("User deleted!")
        this.getUsers()
      },
      error: (error: Error) => {
        alert(error.message) // Display the error message
      }
    });
  }

  public hasPermission(user: User, permission: string): boolean {
    return this.userService.hasPermission(user, permission)
  }


}
