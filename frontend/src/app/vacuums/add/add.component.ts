import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {VacuumService} from "../../services/vacuum.service";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent {

  vacuumForm: FormGroup

  constructor(private vacuumService: VacuumService, private formBuilder: FormBuilder) {
    this.vacuumForm = this.formBuilder.group({
      name: ['', Validators.required]
    })
  }

  public createVacuum() {
    this.vacuumService.addVacuum(this.vacuumForm.get('name')?.value).subscribe(vacuum => {
      alert("Vacuum "+vacuum.name+" added!")
    })
  }
}
