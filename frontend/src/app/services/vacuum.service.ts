import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {User, Vacuum} from "../model";
import {catchError, Observable, throwError} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class VacuumService {

  private readonly apiUrl = environment.vacuums
  private httpOptions = {headers: new HttpHeaders()}

  constructor(private httpClient: HttpClient) { }

  public getVacuums(): Observable<Vacuum[]> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.get<Vacuum[]>(this.apiUrl, this.httpOptions)
  }

  public filteredSearch(name: string, status: string[], dateFrom: string, dateTo: string): Observable<Vacuum[]> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    let httpHeaders = this.httpOptions.headers
    let httpParams = new HttpParams();
    if(name !== "")
      httpParams = httpParams.append('name', name)
    for(let i = 0; i < status.length; i++)
      httpParams = httpParams.append('status', status[i])
    if(dateFrom !== "" && dateTo !== "") {
      httpParams = httpParams.append('dateFrom', dateFrom)
      httpParams = httpParams.append('dateTo', dateTo)
    }

    console.log("Name:"+name)
    console.log("Status:"+status)
    console.log("Date from:"+dateFrom)
    console.log("Date to:"+dateTo)
    console.log(httpParams.keys())

    return this.httpClient.get<Vacuum[]>(this.apiUrl, {headers: httpHeaders, params:httpParams})
  }

  public addVacuum(name: string): Observable<Vacuum> {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    let httpHeaders = this.httpOptions.headers
    let httpParams = new HttpParams()
    httpParams = httpParams.append('name', name)

    return this.httpClient.post<Vacuum>(this.apiUrl, null, {headers: httpHeaders, params: httpParams})
  }

  public removeVacuum(vacuum: Vacuum) {
    let jwt = localStorage.getItem("jwt")
    if(jwt != null)
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', 'Bearer ' + jwt)

    return this.httpClient.delete(this.apiUrl+'/'+vacuum.id, this.httpOptions).pipe(
      catchError(this.handleError)
    )
  }

  private handleError(error: HttpErrorResponse) {
    // Extract the error message from the response body
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = error.error || `Server returned code ${error.status}`;
    }

    return throwError(() => new Error(errorMessage));
  }
}
