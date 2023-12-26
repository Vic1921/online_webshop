import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { LoginService } from '../../services/login.service';
import { OrderService } from '../../services/order.service';
import { ReviewService } from '../../services/review.service';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-writereview',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FooterComponent, HeaderComponent],
  templateUrl: './writereview.component.html',
  styleUrl: './writereview.component.css'
})
export class WriteReviewComponent {
  @Input() productId: number | undefined;
  selectedRating: number | null = null;

  reviewForm = new FormGroup({
    comment: new FormControl(''),
    rating: new FormControl<number>(0, [Validators.required, Validators.min(1), Validators.max(5)]),
  });

  constructor(private reviewService: ReviewService, private loginService: LoginService, private orderService: OrderService) { }


  onStarChange(rating: number): void {
      console.log(rating);
      console.log(this.reviewForm.get('rating')?.value);
      this.reviewForm.get('rating')?.setValue(rating);
      this.selectedRating = rating;
  }

  onSubmit(): void {
    if (this.reviewForm.valid && this.productId != null) {
      const customerId = this.loginService.getCustomerID();
      const formValues = this.reviewForm.value;
      const comment = formValues.comment ?? '';
      const rating = formValues.rating !== undefined && formValues.rating !== null ? +formValues.rating : null;

      let orderId = 0;
      this.orderService.getOrderDetails(customerId, this.productId).subscribe(
        (order) => {
          orderId = order.orderId;
          this.reviewService.addReview(customerId, this.productId!, comment, rating!, orderId).subscribe(
            response => {
              // Handle success
              console.log('Review added successfully', response);
            },
            error => {
              console.log(customerId);
              console.log(this.productId);
              console.log(orderId);
              console.log(rating);
              console.log(comment);
              console.error('Error adding review', error);
            }
          );
          console.log('Fetching Order by customer and product successfully', orderId);
        },
        error => {
          console.error('Fetching Order by customer and product failed', error);
        }
      );
    }else{
      console.log("nothing is happening");
      console.log(this.productId);
    }
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
}
