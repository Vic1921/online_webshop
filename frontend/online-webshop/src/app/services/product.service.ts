import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Product } from '../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8089';
  products : Product[] = [];
  constructor(private http : HttpClient) { }

  getProductsFromHttp(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiUrl}/api/products/get-products`)
    .pipe(
      tap(
        response => console.log('Response:', response),
        error => console.error('Error:', error)
      ),
    );
  }

  getProducts(): Observable<Product[]> {
    // If products array is already populated, return it as an observable
    if (this.products.length > 0) {
      return new Observable<Product[]>((observer) => {
        observer.next(this.products);
        observer.complete();
      });
    } else {
      // If products array is not populated, fetch from HTTP
      return this.getProductsFromHttp();
    }
  }


  getProduct(productId : number) : Observable<Product>{
    return this.http.get<Product>(`${this.apiUrl}/api/products/${productId}`);
  }
}
