import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../interfaces/review';
import { Observable } from 'rxjs';
import { EventEmitterService } from './eventemitter.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8089';

  constructor(private http : HttpClient, private eventEmitterService : EventEmitterService) { }

  getReviewsByProductId(productId : number) : Observable<Review[]>{
    const url = `${this.apiUrl}/api/reviews/product/${productId}`;
    return this.http.get<Review[]>(url);
  }

  addReview(customerId : number, productId : number, comment : string, rating : number, orderId : number) : Observable<Review> {
    const endpoint = `${this.apiUrl}/api/reviews`;
    const request = {
      customerId: customerId,
      productId: productId,
      comment : comment,
      rating : rating,
      orderId : orderId
    };

    this.eventEmitterService.reviewUpdated();

    return this.http.post<Review>(endpoint, request);

  }

}
