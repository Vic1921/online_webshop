import { HttpClient } from '@angular/common/http';
import { NonNullAssert } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8089';
  private tokenKey = 'token';
  private customerKey = 'customerID';
  private cartID = 'cartID';
  private customerName = 'customerName';
  private customerAddress = 'customerAddress';
  public loggedIn: boolean = false;
  constructor(private http: HttpClient, public configService: ConfigService) { }

  login(credentials: any): Observable<any> {
    if (this.configService.useNoSQL == false) {
      return this.http.post<any>(`${this.apiUrl}/api/sql/customers/login`, credentials).pipe(
        tap((response: { token: string; customerID: string; cartID: string; customerName: string; customerAddress: string }) => {
          if (response.token) {
            // Store the token and customer information in local storage
            localStorage.setItem(this.tokenKey, response.token);
            localStorage.setItem(this.customerKey, response.customerID);
            localStorage.setItem(this.cartID, response.cartID);
            localStorage.setItem(this.customerName, response.customerName);
            localStorage.setItem(this.customerAddress, response.customerAddress);
          }
        }),
        catchError(error => {
          console.error('Login failed', error);
          throw error; // Rethrow the error to propagate it to subscribers
        })
      );
    }

    return this.loginNoSQL(credentials);
  }

  loginNoSQL(credentials: any): Observable<any> {
    const loginEndpoint = `${this.apiUrl}/api/nosql/customers/login`;

    return this.http.post(loginEndpoint, credentials, { responseType: 'text' }).pipe(
      tap((response: any) => {
        this.loggedIn = true;
        localStorage.setItem(this.tokenKey, 'your-authentication-token');
        console.log('Login successful', response);
      }),
      catchError((error) => {
        console.error('Login failed', error);
        throw error;
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

  getCustomerName(): string | null {
    return localStorage.getItem(this.customerName);
  }

  getCustomerAddress(): string | null {
    return localStorage.getItem(this.customerAddress);
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
    if (custerCartString == "null") {
      console.log('Returning null', custerCartString);
      return null;
    }
    const customerCartID = custerCartString ? parseInt(custerCartString, 10) : null;
    return customerCartID;
  }

  setCartID(cartID: number | null): void {
    const cartIDString = cartID !== null ? cartID.toString() : "null";
    localStorage.setItem(this.cartID, cartIDString);
  }

  isLoggedIn(): boolean {
    if (this.configService.useNoSQL == false) {
      return !!this.getToken();
    }
    return this.loggedIn;
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.customerKey);
    localStorage.removeItem(this.cartID);

    this.loggedIn = false;
  }
}
