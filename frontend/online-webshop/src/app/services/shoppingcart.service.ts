import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ShoppingCart } from '../interfaces/shoppingcart';
import { ProductDTO } from '../interfaces/product';
import { Cartitem } from '../interfaces/cartitem';
import { EventEmitter } from '@angular/core';
import { EventEmitterService } from './eventemitter.service';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {
  private apiUrl = 'https://localhost:8089';
  constructor(private configService : ConfigService, private http : HttpClient, private eventEmitterService : EventEmitterService) { }

  getCart(cartID : number, customerId : number) : Observable<ShoppingCart>{
      return this.getCartSQL(cartID);
  }

  getCartSQL(cartID : number) : Observable<ShoppingCart>{
    return this.http.get<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/get?id=${cartID}`);
  }

  getCartNoSQL(customerId : string) : Observable<ShoppingCart>{
    return this.http.get<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/get?id=${customerId}`);
  }



  createCart(customerId : number) : Observable<ShoppingCart>{
      return this.createCartFromSQL(customerId);
  }

  createCartFromSQL(customerId : number) : Observable<ShoppingCart>{
    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/create`, customerId);
  }

  createCartFromNoSQL(customerId : string) : Observable<ShoppingCart>{
    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/create`, customerId);

  }


  updateCart(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
    return this.http.put<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/update`, shoppingCart);
  }

  addToCart(customerId: number, shoppingCartId: number, productId: number): Observable<ShoppingCart> {
      return this.addToCartFromSQL(customerId, shoppingCartId, productId);
  }

  addToCartFromSQL(customerId: number, shoppingCartId: number, productId: number): Observable<ShoppingCart>{
    const request = {
      customerId: customerId,
      shoppingCartId: shoppingCartId,
      productId: productId
    };

    this.eventEmitterService.emitCartUpdated();

    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/add-item/`, request);
  }

  addToCartFromNoSQL(customerId: string, productId: number): Observable<ShoppingCart>{
    const request = {
      customerId: customerId,
      productId: productId
    };

    this.eventEmitterService.emitCartUpdated();

    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/add-item/`, request);
  }

  getShoppingCartItems(cartID : number, customerId : number) : Observable<Cartitem[]>{
      const url = `${this.apiUrl}/api/sql/shopping-cart/get-items?id=${cartID}`;
      return this.http.get<Cartitem[]>(url);
  }

  getShoppingCartItemsNoSQL(customerId : string) : Observable<Cartitem[]> {
    const url = `${this.apiUrl}/api/nosql/shopping-cart/get-items?id=${customerId}`;
    return this.http.get<Cartitem[]>(url);
  }

  deleteCart(cartID : number) : Observable<any>{
    const options = { body: { id: cartID } };
    return this.http.delete<any>(`${this.apiUrl}/api/sql/shopping-cart/delete`, options);
  }
}
