import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { ProductService } from '../../services/product.service';
import { switchMap } from 'rxjs';
import { DbfillerService } from '../../services/dbfiller.service';
import { Product } from '../../interfaces/product';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent, RouterModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent{
  products : Product[] = [];

  constructor(private productService : ProductService){
    this.productService.getProductsFromHttp().subscribe(
      (data: any[]) => {
        this.productService.products = data;
        this.products = this.productService.products;
      },
      (error: any) => {
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
