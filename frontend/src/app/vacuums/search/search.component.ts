import { Component } from '@angular/core';
import {Vacuum} from "../../model";
import {VacuumService} from "../../services/vacuum.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [DatePipe]
})
export class SearchComponent {

  vacuums: Vacuum[]

  name: string
  dateFrom: string
  dateTo: string
  stopped: boolean
  running: boolean
  discharging: boolean

  public constructor(private vacuumService: VacuumService, private datePipe: DatePipe) {
    this.vacuums = []
    this.name = ""
    this.dateFrom = ""
    this.dateTo = ""
    this.stopped = false
    this.running = false
    this.discharging = false
  }

  ngOnInit(): void {
    this.getVacuums()
  }

  public getVacuums() {
    //GET http://localhost:8080/api/vacuums
    this.vacuumService.getVacuums().subscribe(vacuums => {
      this.vacuums = vacuums
    });
  }

  public filteredSearch() {
    if(this.checkDatesFilter()) {
      this.vacuumService.filteredSearch(this.name, this.getStatus(), this.dateFrom, this.dateTo).subscribe(vacuums => {
        this.vacuums = vacuums
      });
    } else {
      alert("Please use 'yyyy-MM-dd' format for both dates")
    }

  }

  public removeVacuum(vacuum: Vacuum) {
    this.vacuumService.removeVacuum(vacuum).subscribe({
      next: () => {
        alert("Vacuum "+vacuum.id+" removed!")
        this.vacuums = this.vacuums.filter(vacuume => vacuume.id != vacuum.id);
      },
        error: (error: Error) => {
        alert(error.message) // Display the error message
      }
    });
  }

  public formatDate(dateCreated: number) : string | null {
    const date = new Date(dateCreated);
    return this.datePipe.transform(date, 'yyyy-MM-dd');
  }

  private checkDatesFilter() : boolean {
    if(this.dateFrom === "" && this.dateTo === "")
      return true

    return this.isValidDateFormat(this.dateFrom) && this.isValidDateFormat(this.dateTo);
  }

  private isValidDateFormat(dateString: string) : boolean {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    if (!regex.test(dateString)) {
      return false;
    }

    const [year, month, day] = dateString.split('-').map(Number);

    if (year < 1000 || year > 9999) return false;
    if (month < 1 || month > 12) return false;

    const daysInMonth = new Date(year, month, 0).getDate();
    return day > 0 && day <= daysInMonth;
  }

  private getStatus() : string[] {
    const status: string[] = [];
    if(this.stopped) status.push("STOPPED")
    if(this.running) status.push("RUNNING")
    if(this.discharging) status.push("DISCHARGING")

    return status
  }

}
