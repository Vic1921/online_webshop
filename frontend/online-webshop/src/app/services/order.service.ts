import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../interfaces/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8089';
  constructor(private http : HttpClient) {}
  
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

    return this.http.post<Order>(`${this.apiUrl}/api/orders/place`, request);
  }

  getOrderByID(orderId : number) : Observable<Order>{
    return this.http.get<Order>(`${this.apiUrl}/api/orders/${orderId}`);
  }

  updateOrder(orderId : number, order: Order): Observable<Order> {
    const url = `${this.apiUrl}/orders/${orderId}`;
    return this.http.put<Order>(url, order);
  }

  getOrdersByCustomerId(customerId : number) : Observable<Order[]>{
    const url = `${this.apiUrl}/${customerId}`;
    return this.http.get<Order[]>(url);

  }
}
