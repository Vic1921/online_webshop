import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SignupService {
  private apiUrl = 'http://localhost:8089';

  constructor(private http: HttpClient) { }

  signUp(registerData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/customers/register`, registerData);
  }
}
