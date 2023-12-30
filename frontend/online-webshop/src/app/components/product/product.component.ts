import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { ProductService } from '../../services/product.service';
import { switchMap } from 'rxjs';
import { DbfillerService } from '../../services/dbfiller.service';
import { ProductDTO } from '../../interfaces/product';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent, RouterModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent{
  products : ProductDTO[] = [];
  vendorNames : string[] = [];
  vendorProductCounts: { [vendorName: string]: number } = {};


  constructor(private productService : ProductService){
    this.productService.getProductsFromHttp().subscribe(
      (data: any[]) => {
        this.productService.products = data;
        this.products = this.productService.products;
        this.extractVendorNames();
        this.calculateVendorProductCounts();
      },
      (error: any) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  extractVendorNames() {
    const vendorNamesSet = new Set<string>();

    this.products.forEach(product => {
      vendorNamesSet.add(product.vendorName);
    });

    this.vendorNames = Array.from(vendorNamesSet);
  }

  calculateVendorProductCounts() {
    this.vendorProductCounts = this.products.reduce((accumulator, product) => {
      const vendorName = product.vendorName;

      // Increment the count for the current vendor or set it to 1 if it doesn't exist
      accumulator[vendorName] = (accumulator[vendorName] || 0) + 1;

      return accumulator;
    }, {} as { [vendorName: string]: number }); // Explicitly specify the type here
  }

 
  getProductImageStyle(imageUrl: string): object {
    return {
      'background-image': `url('./assets/images/products/${imageUrl}')`,
      'background-repeat': 'no-repeat',
      'background-size': 'contain',
      'background-position': 'center center'
    };
  }
  


}
