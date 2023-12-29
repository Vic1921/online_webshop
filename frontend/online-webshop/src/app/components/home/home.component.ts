import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ReportService } from '../../services/report.service';
import { ProductDTO } from '../../interfaces/product';
import { Review } from '../../interfaces/review';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  bestsellers: ProductDTO[] = [];
  topReviewers: Review[] = [];
  constructor(private reportService: ReportService) {
    this.reportService.getBestsellers(0, 1000, 3).subscribe(
      response => {
        this.bestsellers = response;
        console.log("Bestsellers successfully fetched: ", response);

        this.reportService.getTopReviewers(50, 5).subscribe(
          response => {
            this.topReviewers = response;
            console.log("Top Reviewers successfully fetched: ", response);
          },
          error => {
            console.log("Error fetching top reviewers: ", error);
          }
        )
      },
      error => {
        console.log("Error fetching bestsellers: ", error);
      }
    )
  }
}
