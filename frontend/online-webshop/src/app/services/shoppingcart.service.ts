import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ShoppingCart } from '../interfaces/shoppingcart';
import { Product } from '../interfaces/product';
import { Cartitem } from '../interfaces/cartitem';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {
  private apiUrl = 'http://localhost:8089';
  constructor(private http : HttpClient) { }

  getCart(cartID : number) : Observable<ShoppingCart>{
    return this.http.get<ShoppingCart>(`${this.apiUrl}/api/shopping-cart/get?id=${cartID}`);
  }

  createCart(customerId : number) : Observable<ShoppingCart>{
    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/shopping-cart/create`, customerId);
  }

  updateCart(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
    return this.http.put<ShoppingCart>(`${this.apiUrl}/api/shopping-cart/update`, shoppingCart);
  }

  addToCart(customerId: number, shoppingCartId: number, productId: number): Observable<ShoppingCart> {
    const request = {
      customerId: customerId,
      shoppingCartId: shoppingCartId,
      productId: productId
    };

    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/shopping-cart/add-item/`, request);
  }

  getShoppingCartItems(cartID : number) : Observable<Cartitem[]>{
    const url = `${this.apiUrl}/api/shopping-cart/get-items?id=${cartID}`;
    return this.http.get<Cartitem[]>(url);
  }

  deleteCart(cartID : number) : Observable<any>{
    const options = { body: { id: cartID } };
    return this.http.delete<any>(`${this.apiUrl}/api/shopping-cart/delete`, options);
  }
}
