import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../interfaces/review';
import { Observable } from 'rxjs';
import { EventEmitterService } from './eventemitter.service';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'https://localhost:8089';

  constructor(private configService : ConfigService, private http : HttpClient, private eventEmitterService : EventEmitterService) { }

  getReviewsByProductId(productId : number) : Observable<Review[]>{
    if(this.configService.useNoSQL == false){
      return this.getReviewsByProductIdFromSQL(productId);
    }

    return this.getReviewsByProductIdFromNoSQL(productId);
  }

  getReviewsByProductIdFromSQL(productId : number) : Observable<Review[]>{
    const url = `${this.apiUrl}/api/sql/reviews/product/${productId}`;
    return this.http.get<Review[]>(url);
  }

  getReviewsByProductIdFromNoSQL(productId : number) : Observable<Review[]>{
    const url = `${this.apiUrl}/api/nosql/reviews/product/${productId}`;
    return this.http.get<Review[]>(url);
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

  addReviewFromNoSQL(customerId : number, productId : number, comment : string, rating : number) : Observable<Review> {
    const endpoint = `${this.apiUrl}/api/nosql/reviews`;
    const request = {
      customerId: customerId,
      productId: productId,
      comment : comment,
      rating : rating,
    };
    this.eventEmitterService.reviewUpdated();

    return this.http.post<Review>(endpoint, request);

  }




}
