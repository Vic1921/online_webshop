import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { RouterModule , RouterLink } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ProductService } from '../services/product.service';
import { DbfillerService } from '../services/dbfiller.service';
import { switchMap, take } from 'rxjs/operators'; // Import switchMap and take from 'rxjs/operators'



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HomeComponent, RouterModule, RouterLink, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Home';
}