import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'https://localhost:8089';

  constructor(private configService : ConfigService, private http: HttpClient) { }

  updateCart(customerId: number, cartId: number): Observable<any> {
    if(this.configService.useNoSQL == false){
      const requestBody = {
        customerId: customerId,
        cartId: cartId
      };
  
      return this.http.post<any>(`${this.apiUrl}/api/sql/customers/update-cart`, requestBody);
    }

    return of(null);
  }
}
