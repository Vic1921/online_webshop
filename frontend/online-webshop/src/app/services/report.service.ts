import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductDTO } from '../interfaces/product';
import { Review } from '../interfaces/review';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = 'https://localhost:8089';
  constructor(private configService : ConfigService, private http : HttpClient) { }

  getBestsellers(minValue : number, maxValue : number, limit : number): Observable<ProductDTO[]>{
    if(this.configService.useNoSQL == false){
      this.getBestsellersFromSQL(minValue, maxValue, limit);
    }

    return this.getBestsellersFromNoSQL(minValue, maxValue, limit);
  }

  getBestsellersFromSQL(minValue : number, maxValue : number, limit : number): Observable<ProductDTO[]>{
    const params = { minValue: String(minValue), maxValue: String(maxValue), limit: String(limit) };

    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/sql/reports/bestsellers`, {params});
  }

  getBestsellersFromNoSQL(minValue : number, maxValue : number, limit : number): Observable<ProductDTO[]>{
    const params = { minValue: String(minValue), maxValue: String(maxValue), limit: String(limit) };

    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/nosql/reports/bestsellers`, {params});
  }

  getTopReviewers(price : number, limit : number): Observable<Review[]>{
    if(this.configService.useNoSQL == false){
      this.getTopReviewersFromSQL(price, limit);
    }

    return this.getTopReviewersFromNoSQL(price, limit);
  }


  getTopReviewersFromSQL(price : number, limit : number): Observable<Review[]>{
    const params = { price: String(price), limit: String(limit)};

    return this.http.get<Review[]>(`${this.apiUrl}/api/sql/reports/top-reviewers`, {params});
  }

  getTopReviewersFromNoSQL(price : number, limit : number): Observable<Review[]>{
    const params = { price: String(price), limit: String(limit)};

    return this.http.get<Review[]>(`${this.apiUrl}/api/nosql/reports/top-reviewers`, {params});
  }



}
