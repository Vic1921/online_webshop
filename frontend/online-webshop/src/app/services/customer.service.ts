import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8089';

  constructor(private http: HttpClient) { }

  updateCart(customerId: number, cartId: number): Observable<any> {
    const requestBody = {
      customerId: customerId,
      cartId: cartId
    };

    return this.http.post<any>(`${this.apiUrl}/api/customers/update-cart`, requestBody);
  }
}
