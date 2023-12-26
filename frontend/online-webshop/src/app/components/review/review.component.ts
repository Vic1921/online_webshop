import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
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

  constructor(private reviewService: ReviewService, private router : Router) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['productId'] && this.productId) {
      this.reviewService.getReviewsByProductId(this.productId).subscribe(
        (reviews: Review[]) => {
          this.reviews = reviews;
          this.averageReviewRating = this.calculateAverageRating();
          console.log('Reviews successfully fetched:', this.reviews);
        },
        (error: any) => {
          console.error('Error fetching reviews:', error);
        }
      );
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

  getReviewSize(){
    return this.reviews.length;
  }

  
  writeReview() {
    this.router.navigate(['/create-review']);
    }
}
