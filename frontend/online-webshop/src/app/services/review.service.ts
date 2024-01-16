import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../interfaces/review';
import { Observable } from 'rxjs';
import { EventEmitterService } from './eventemitter.service';
import { ConfigService } from '../config.service';
import { ReviewNoSQL } from '../interfaces/reviewnosql';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'https://localhost:8089';

  constructor(private configService : ConfigService, private http : HttpClient, private eventEmitterService : EventEmitterService) { }

  getReviewsByProductIdFromSQL(productId : number) : Observable<Review[]>{
    const url = `${this.apiUrl}/api/sql/reviews/product/${productId}`;
    return this.http.get<Review[]>(url);
  }

  getReviewsByProductIdFromNoSQL(productId : number) : Observable<ReviewNoSQL[]>{
    const url = `${this.apiUrl}/api/nosql/reviews/product/${productId}`;
    return this.http.get<ReviewNoSQL[]>(url);
  }


  addReviewFromSQL(customerId : number, productId : number, comment : string, rating : number, orderId : number) : Observable<Review> {
    const endpoint = `${this.apiUrl}/api/sql/reviews`;
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

  addReviewFromNoSQL(customerId : string, productId : number, comment : string, rating : number) : Observable<ReviewNoSQL> {
    const endpoint = `${this.apiUrl}/api/nosql/reviews`;
    const request = {
      customerId: customerId,
      productId: productId,
      comment : comment,
      rating : rating,
    };
    this.eventEmitterService.reviewUpdated();

    return this.http.post<ReviewNoSQL>(endpoint, request);

  }




}
