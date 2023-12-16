import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8089';
  private tokenKey = 'token';
  private customerKey ='customer';
  constructor(private http : HttpClient) { }

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/customers/login`, credentials).pipe(
      tap((response: { token: string; customer: any; }) => {
        if (response.token) {
          // Store the token and customer information in local storage
          localStorage.setItem(this.tokenKey, response.token);
          localStorage.setItem(this.customerKey, JSON.stringify(response.customer));
        }
      }),
      catchError(error => {
        console.error('Login failed', error);
        throw error; // Rethrow the error to propagate it to subscribers
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getCustomer(): any | null {
    const customerString = localStorage.getItem(this.customerKey);
    return customerString ? JSON.parse(customerString) : null;
  }

  isLoggedIn(): boolean{
    return !!this.getToken();
  }

  logout() : void{
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.customerKey);
  }
}
