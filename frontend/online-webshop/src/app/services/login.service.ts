import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8089';
  constructor(private http : HttpClient) { }

  login(loginData : any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/customers/login`, loginData);
  }
}
