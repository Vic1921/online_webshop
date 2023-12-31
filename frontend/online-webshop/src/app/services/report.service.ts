import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductDTO } from '../interfaces/product';
import { Review } from '../interfaces/review';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = 'http://localhost:8089';
  constructor(private http : HttpClient) { }

  getBestsellers(minValue : number, maxValue : number, limit : number): Observable<ProductDTO[]>{
    const params = { minValue: String(minValue), maxValue: String(maxValue), limit: String(limit) };

    return this.http.get<ProductDTO[]>(`${this.apiUrl}/api/reports/bestsellers`, {params});
  }

  getTopReviewers(price : number, limit : number): Observable<Review[]>{
    const params = { price: String(price), limit: String(limit)};

    return this.http.get<Review[]>(`${this.apiUrl}/api/reports/top-reviewers`, {params});
  }

}
