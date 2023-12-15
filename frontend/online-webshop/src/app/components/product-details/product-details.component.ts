import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../../product';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent {
  product: Product | undefined;

  constructor(private route: ActivatedRoute, private productService: ProductService) {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    console.info(productId);

    this.productService.getProduct(productId).subscribe((product) => {
      this.product = product;
    }),
      (error: any) => {
        console.error("Error fethcing product: ", error);
      }


  }


  getProductImageStyle(imageUrl: string): object {
    return {
      'background-image': `url(${imageUrl})`,
      'background-repeat': 'no-repeat',
      'background-size': 'cover',
      'background-position': 'center center'
    };
  }
}
