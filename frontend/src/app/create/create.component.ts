import {Component, OnInit} from '@angular/core';
import {UserService} from "../services/user.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  create: boolean = false
  read: boolean = false
  update: boolean = false
  delete: boolean = false
  add: boolean = false
  search: boolean = false
  remove: boolean = false
  start: boolean = false
  stop: boolean = false
  discharge: boolean = false

  userForm: FormGroup

  constructor(private userService: UserService, private formBuilder: FormBuilder) {
    this.userForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(3)]],
      name: ['', Validators.required],
      surname: ['', Validators.required],
    })
  }

  ngOnInit() {
  }

  public flipCreate() {
    this.create = !this.create
  }

  public flipRead() {
    this.read = !this.read
  }

  public flipUpdate() {
    this.update = !this.update
  }

  public flipDelete() {
    this.delete = !this.delete
  }

  public flipAdd() {
    this.delete = !this.delete
  }

  public flipSearch() {
    this.delete = !this.delete
  }

  public flipRemove() {
    this.delete = !this.delete
  }

  public flipStart() {
    this.delete = !this.delete
  }

  public flipStop() {
    this.delete = !this.delete
  }

  public flipDischarge() {
    this.delete = !this.delete
  }

  public createUser() {
    this.userService.createUser(
      this.userForm.get('name')?.value,
      this.userForm.get('surname')?.value,
      this.userForm.get('email')?.value,
      this.userForm.get('password')?.value,
      this.setPermissions()
    ).subscribe((user) => {
      alert("User created!")
      this.userForm.reset()
    }, (error) => {
      alert("Error creating user, error message: "+error.message())
    })
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
