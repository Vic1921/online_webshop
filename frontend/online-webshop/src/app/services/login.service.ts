import { HttpClient } from '@angular/common/http';
import { NonNullAssert } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8089';
  private tokenKey = 'token';
  private customerKey ='customerID';
  private cartID = 'cartID';
  constructor(private http : HttpClient) { }

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/customers/login`, credentials).pipe(
      tap((response: { token: string; customerID: string; cartID: string}) => {
        if (response.token) {
          // Store the token and customer information in local storage
          localStorage.setItem(this.tokenKey, response.token);
          localStorage.setItem(this.customerKey, response.customerID);
          localStorage.setItem(this.cartID, response.cartID);
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

  getCustomerID(): number {
    const customerString = localStorage.getItem(this.customerKey);
    const customerId = customerString ? parseInt(customerString, 10) : 0; 
    return customerId
  }


  /*
  getCustomer(): any | null {
    const customerString = localStorage.getItem(this.customerKey);
    return customerString ? JSON.parse(customerString) : null;
  }
  */

  getCartID(): number | null {
    const custerCartString = localStorage.getItem(this.cartID);
    console.log(custerCartString);
    if(custerCartString == "null"){
      console.log('Returning null', custerCartString);
      return null;
    }
    const customerCartID = custerCartString ? parseInt(custerCartString, 10) : null; 
    return customerCartID;
}

  isLoggedIn(): boolean{
    return !!this.getToken();
  }

  logout() : void{
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.customerKey);
    localStorage.removeItem(this.cartID);
  }
}
