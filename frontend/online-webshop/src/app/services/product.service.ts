import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Product } from '../product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8089';
  constructor(private http : HttpClient) { }

  getProducts(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiUrl}/api/products/get-products`)
    .pipe(
      tap(
        response => console.log('Response:', response),
        error => console.error('Error:', error)
      ),
    );
  }

  getProduct(productId : number) : Observable<Product>{
    return this.http.get<Product>(`${this.apiUrl}/api/products/${productId}`);
  }
}
