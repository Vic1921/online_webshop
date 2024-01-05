import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../interfaces/order';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8089';
  constructor(private configService : ConfigService, private http : HttpClient) {}
  
  placeOrder(
    customerId: number,
    shoppingCartId: number,
    paymentMethod: string,
    shippingDetails: string
  ): Observable<Order> {
    const request = {
      customerId,
      shoppingCartId,
      paymentMethod,
      shippingDetails,
    };

    return this.http.post<Order>(`${this.apiUrl}/api/sql/orders/place`, request);
  }

  getOrderByID(orderId : number) : Observable<Order>{
    return this.http.get<Order>(`${this.apiUrl}/api/sql/orders/${orderId}`);
  }

  updateOrder(orderId : number, order: Order): Observable<Order> {
    const url = `${this.apiUrl}/orders/${orderId}`;
    return this.http.put<Order>(url, order);
  }

  getOrdersByCustomerId(customerId : number) : Observable<Order[]>{
    if(this.configService.useNoSQL == false){
      return this.getOrdersByCustomerIdFromSQL(customerId);
    }

    return this.getOrdersByCustomerIdFromNoSQL(customerId);
  }

  getOrdersByCustomerIdFromSQL(customerId : number) : Observable<Order[]>{
    const url = `${this.apiUrl}/api/sql/orders/=${customerId}`;
    return this.http.get<Order[]>(url);
  }

  getOrdersByCustomerIdFromNoSQL(customerId : number) : Observable<Order[]>{
    const url = `${this.apiUrl}/api/nosql/orders/=${customerId}`;
    return this.http.get<Order[]>(url);
  }

  getOrderDetails(customerId: number, productId: number): Observable<Order> {
    const url = `${this.apiUrl}/api/sql/orders/order-details?customerId=${customerId}&productId=${productId}`;

    return this.http.get<Order>(url);
  }
  
}
