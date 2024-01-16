import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { ProductDTO } from '../interfaces/product';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'https://localhost:8089';
  products : ProductDTO[] = [];
  constructor(private configService : ConfigService, private http : HttpClient) { }

  getProductsFromHttp(): Observable<ProductDTO[]>{

    if(this.configService.useNoSQL == false){
      return this.getProductsFromSQL();
    }

    return this.getProductsFromNoSQL();
  }

  getProductsFromSQL(): Observable<ProductDTO[]>{
    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/sql/products/get-products`)
    .pipe(
      tap(
        response => console.log('Response:', response),
        error => console.error('Error:', error)
      ),
    );
  }

  getProductsFromNoSQL() : Observable<ProductDTO[]>{
    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/nosql/products/get-products`)
    .pipe(
      tap(
        response => console.log('Response:', response),
        error => console.error('Error:', error)
      )
    )

  }

  getProductFromSQL(productId: number) : Observable<ProductDTO>{
    return this.http.get<ProductDTO>(`${this.apiUrl}/api/sql/products/${productId}`);
  }

  getProductFromNoSQL(productId: number) : Observable<ProductDTO>{
    return this.http.get<ProductDTO>(`${this.apiUrl}/api/nosql/products/${productId}`);
  }


  getProduct(productId : number) : Observable<ProductDTO>{
    if(this.configService.useNoSQL == false){
      return this.getProductFromSQL(productId);
    }

    return this.getProductFromNoSQL(productId);
  }
}
