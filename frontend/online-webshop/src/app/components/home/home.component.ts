import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ReportService } from '../../services/report.service';
import { ProductDTO } from '../../interfaces/product';
import { Review } from '../../interfaces/review';
import { ConfigService } from '../../config.service';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent{
  bestsellers: ProductDTO[] = [];
  topReviewers: Review[] = [];
  constructor(private configService : ConfigService, private reportService: ReportService) {
    console.log("THIS IS THE CONFIG RIGHT NOW");
    console.log(configService.useNoSQL);
    
    if(this.configService.useNoSQL == false){
      this.reportService.getBestsellersFromSQL(0, 1000, 3).subscribe(
        response => {
          this.bestsellers = response;
          console.log("Bestsellers successfully fetched: ", response);
        },
        error => {
          console.log("Error fetching bestsellers: ", error);
        }
      )
    }else{
      this.reportService.getBestsellersFromNoSQL(0, 1000, 3).subscribe(
        response => {
          this.bestsellers = response;
          console.log("Bestsellers successfully fetched: ", response);
        },
        error => {
          console.log("Error fetching bestsellers: ", error);
        }
      )
    }

    
    if(this.configService.useNoSQL == false){
      this.reportService.getTopReviewersFromSQL(50, 5).subscribe(
        response => {
          this.topReviewers = response;
          console.log("Top Reviewers successfully fetched: ", response);
        },
        error => {
          console.log("Error fetching top reviewers: ", error);
        }
      )
    }else{
      this.reportService.getTopReviewersFromNoSQL(50, 5).subscribe(
        response => {
          this.topReviewers = response;
          console.log("Top Reviewers successfully fetched: ", response);
        },
        error => {
          console.log("Error fetching top reviewers: ", error);
        }
      )
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
