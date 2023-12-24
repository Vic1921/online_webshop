import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../interfaces/review';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8089';

  constructor(private http : HttpClient) { }

  getReviewsByProductId(productId : number) : Observable<Review[]>{
    const url = `${this.apiUrl}/api/reviews/product/${productId}`;
    return this.http.get<Review[]>(url);

  }

}
