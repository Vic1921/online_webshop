import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../interfaces/order';
import { ConfigService } from '../config.service';
import { OrderNoSQL } from '../interfaces/ordernosql';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'https://localhost:8089';
  constructor(private configService: ConfigService, private http: HttpClient) { }

  placeOrderFromSQL(customerId: number, shoppingCartId: number, paymentMethod: string, shippingDetails: string): Observable<Order> {
    const request = {
      customerId,
      shoppingCartId,
      paymentMethod,
      shippingDetails,
    };

    return this.http.post<Order>(`${this.apiUrl}/api/sql/orders/place`, request);
  }

  placeOrderFromNoSQL(customerId: string, paymentMethod: string, shippingDetails: string): Observable<OrderNoSQL> {
    const request = {
      customerId,
      paymentMethod,
      shippingDetails
    }

    return this.http.post<OrderNoSQL>(`${this.apiUrl}/api/nosql/orders/place`, request);
  }

  getOrderByIDFromSQL(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/api/sql/orders/${orderId}`);
  }

  getOrderByIDFromNoSQL(orderId: string): Observable<OrderNoSQL> {
    return this.http.get<OrderNoSQL>(`${this.apiUrl}/api/nosql/orders/${orderId}`);
  }

  updateOrder(orderId: number, order: Order): Observable<Order> {
    const url = `${this.apiUrl}/orders/${orderId}`;
    return this.http.put<Order>(url, order);
  }


  getOrdersByCustomerIdFromSQL(customerId: number): Observable<Order[]> {
    const url = `${this.apiUrl}/api/sql/orders/=${customerId}`;
    return this.http.get<Order[]>(url);
  }

  getOrdersByCustomerIdFromNoSQL(customerId: string): Observable<OrderNoSQL[]> {
    const url = `${this.apiUrl}/api/nosql/orders/=${customerId}`;
    return this.http.get<OrderNoSQL[]>(url);
  }

  getOrderDetails(customerId: number, productId: number): Observable<Order> {
    const url = `${this.apiUrl}/api/sql/orders/order-details?customerId=${customerId}&productId=${productId}`;

    return this.http.get<Order>(url);
  }

}
