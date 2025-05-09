import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {UsersComponent} from "./users/users.component";
import {ReadGuard} from "./guards/read.guard";
import {EditComponent} from "./edit/edit.component";
import {UpdateGuard} from "./guards/update.guard";
import {CreateComponent} from "./create/create.component";
import {CreateGuard} from "./guards/create.guard";
import {AddComponent} from "./vacuums/add/add.component";
import {SearchComponent} from "./vacuums/search/search.component";
import {SearchGuard} from "./guards/search.guard";
import {AddGuard} from "./guards/add.guard";

const routes: Routes = [
  {
    path: "",
    component: LoginComponent
  },
  {
    path: "users",
    component: UsersComponent,
    canActivate: [ReadGuard]
  },
  {
    path: "edit",
    component: EditComponent,
    canActivate: [UpdateGuard]
  },
  {
    path: "create",
    component: CreateComponent,
    canActivate: [CreateGuard]
  },
  {
    path: "search",
    component: SearchComponent,
    canActivate: [SearchGuard]
  },
  {
    path: "add",
    component: AddComponent,
    canActivate: [AddGuard]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
