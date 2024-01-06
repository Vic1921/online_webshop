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
  private apiUrl = 'http://localhost:8089';
  constructor(private configService : ConfigService, private http : HttpClient, private eventEmitterService : EventEmitterService) { }

  getCart(cartID : number, customerId : number) : Observable<ShoppingCart>{
    if(this.configService.useNoSQL == false){
      return this.getCartSQL(cartID);
    }

    return this.getCartNoSQL(customerId);
  }

  getCartSQL(cartID : number) : Observable<ShoppingCart>{
    return this.http.get<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/get?id=${cartID}`);
  }

  getCartNoSQL(customerId : number) : Observable<ShoppingCart>{
    return this.http.get<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/get?id=${customerId}`);
  }



  createCart(customerId : number) : Observable<ShoppingCart>{
    if(this.configService.useNoSQL == false){
      return this.createCartFromSQL(customerId);
    }

    return this.createCartFromNoSQL(customerId);
  }

  createCartFromSQL(customerId : number) : Observable<ShoppingCart>{
    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/create`, customerId);
  }

  createCartFromNoSQL(customerId : number) : Observable<ShoppingCart>{
    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/create`, customerId);

  }


  updateCart(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
    return this.http.put<ShoppingCart>(`${this.apiUrl}/api/sql/shopping-cart/update`, shoppingCart);
  }

  addToCart(customerId: number, shoppingCartId: number, productId: number): Observable<ShoppingCart> {
    if(this.configService.useNoSQL == false){
      return this.addToCartFromSQL(customerId, shoppingCartId, productId);
    }

    return this.addToCartFromNoSQL(customerId, productId);
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

  addToCartFromNoSQL(customerId: number, productId: number): Observable<ShoppingCart>{
    const request = {
      customerId: customerId,
      productId: productId
    };

    this.eventEmitterService.emitCartUpdated();

    return this.http.post<ShoppingCart>(`${this.apiUrl}/api/nosql/shopping-cart/add-item/`, request);
  }

  getShoppingCartItems(cartID : number, customerId : number) : Observable<Cartitem[]>{
    if (this.configService.useNoSQL == false) {
      const url = `${this.apiUrl}/api/sql/shopping-cart/get-items?id=${cartID}`;
      return this.http.get<Cartitem[]>(url);
    }

    return this.getShoppingCartItemsNoSQL(customerId);
  }

  getShoppingCartItemsNoSQL(customerId : number) : Observable<Cartitem[]> {
    const url = `${this.apiUrl}/api/nosql/shopping-cart/get-items?id=${customerId}`;
    return this.http.get<Cartitem[]>(url);
  }

  deleteCart(cartID : number) : Observable<any>{
    const options = { body: { id: cartID } };
    return this.http.delete<any>(`${this.apiUrl}/api/sql/shopping-cart/delete`, options);
  }
}
