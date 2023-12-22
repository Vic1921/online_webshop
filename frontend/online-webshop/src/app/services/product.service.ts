import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { ProductDTO } from '../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8089';
  products : ProductDTO[] = [];
  constructor(private http : HttpClient) { }

  getProductsFromHttp(): Observable<ProductDTO[]>{
    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/products/get-products`)
    .pipe(
      tap(
        response => console.log('Response:', response),
        error => console.error('Error:', error)
      ),
    );
  }

  getProducts(): Observable<ProductDTO[]> {
    // If products array is already populated, return it as an observable
    if (this.products.length > 0) {
      return new Observable<ProductDTO[]>((observer) => {
        observer.next(this.products);
        observer.complete();
      });
    } else {
      // If products array is not populated, fetch from HTTP
      return this.getProductsFromHttp();
    }
  }


  getProduct(productId : number) : Observable<ProductDTO>{
    return this.http.get<ProductDTO>(`${this.apiUrl}/api/products/${productId}`);
  }
}
