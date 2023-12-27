import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Review } from '../../interfaces/review';
import { ReviewService } from '../../services/review.service';
import { Router } from '@angular/router';
import { WriteReviewComponent } from '../writereview/writereview.component';

@Component({
  selector: 'app-review',
  standalone: true,
  imports: [CommonModule, WriteReviewComponent],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css'
})
export class ReviewComponent implements OnChanges{

  reviews: Review[] = [];
  @Input() productId: number | undefined;
  averageReviewRating: number | undefined;

  @Output() averageRatingEmitter: EventEmitter<number> = new EventEmitter<number>();


  constructor(private reviewService: ReviewService, private router : Router) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['productId'] && this.productId) {
      this.updateReviews(); // Initial update
    }
  }

  calculateAverageRating(): number {
    if (this.reviews.length === 0) {
      return 0; // Return 0 if there are no reviews
    }

    const totalRating = this.reviews.reduce((sum, review) => sum + review.reviewRating, 0);
    const averageRating = totalRating / this.reviews.length;

    // Round the average to 1 decimal place
    return Math.round(averageRating * 10) / 10;
  }

  getStarRating(rating: number): string[] {
    const maxRating = 5;
    const filledStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0; 

    const starRating: string[] = Array(maxRating).fill('fa fa-star');

    for (let i = 0; i < filledStars; i++) {
      starRating[i] = 'fa fa-star rating-color';
    }

    if (hasHalfStar && filledStars < maxRating) {
      starRating[filledStars] = 'fa fa-star-half-alt rating-color';
    }

    return starRating;
  }

  updateReviews() {
    if (this.productId) {
      this.reviewService.getReviewsByProductId(this.productId).subscribe(
        (reviews: Review[]) => {
          this.reviews = reviews;
          this.averageReviewRating = this.calculateAverageRating();
          this.averageRatingEmitter.emit(this.averageReviewRating);
          console.log('Reviews successfully updated:', this.reviews);
        },
        (error: any) => {
          console.error('Error updating reviews:', error);
        }
      );
    }
  }

  getReviewSize(){
    return this.reviews.length;
  }
}
