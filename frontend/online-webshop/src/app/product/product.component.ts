import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../components/footer/footer.component';
import { HeaderComponent } from '../components/header/header.component';
import { ProductService } from '../product.service';
import { switchMap } from 'rxjs';
import { DbfillerService } from '../dbfiller.service';
import { Product } from '../product';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit{
  products : Product[] = [];

  constructor(private productService : ProductService, private databaseFillerService : DbfillerService){}

  ngOnInit(): void {
    this.databaseFillerService.fillDatabase().pipe(
      switchMap(() => this.productService.getProducts())
    ).subscribe(
      (data: any[]) => {
        this.products = data;
        console.log(data);
      },
      (error : any) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  getProductImageStyle(imageUrl: string): object {
    return {
      'background-image': `url('./assets/images/products/${imageUrl}')`,
      'background-repeat': 'no-repeat',
      'background-size': 'cover',
      'background-position': 'center center'
    };
  }
  


}
