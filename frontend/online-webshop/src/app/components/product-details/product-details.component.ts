import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { LoginService } from '../../services/login.service';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { CustomerService } from '../../services/customer.service';
import { ProductDTO } from '../../interfaces/product';
import { ReviewComponent } from '../review/review.component';
import { ConfigService } from '../../config.service';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent, ReviewComponent],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent {
  product: ProductDTO | undefined;
  productId!: number;
  averageRating: number | undefined;

  constructor(private configService: ConfigService, private route: ActivatedRoute, private productService: ProductService, private loginService: LoginService, private shoppingCartService: ShoppingcartService, private customerService: CustomerService) {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    console.log("This is the product id from product details", this.productId);
    this.productService.getProduct(this.productId).subscribe((product) => {
      this.product = product;
      console.log('This are is the review length: ', this.product.reviewIds);
      console.log('Product details fetched:', product);
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

  searchArticle() {
    throw new Error('Method not implemented.');
  }
  addToCart(productId: number | undefined): void {
    if (productId == undefined) {
      console.error('Product Id is undefined');
      return;
    }

    if (this.loginService.isLoggedIn()) {
      if (this.configService.useNoSQL == false) {
        const customerId = this.loginService.getCustomerID();
        // Check if the customer has an existing cart
        let cartId: number | null = this.loginService.getCartID();
        if (cartId == null) {
          console.info("Cart id should be null" + cartId);
        } else {
          console.info("Cart id is not null" + cartId);
        }

        if (cartId !== null) {
          // If the customer has an existing cart, add the product to the existing cart
          console.info(cartId);
          this.shoppingCartService.addToCartFromSQL(customerId, cartId, productId).subscribe(
            (shoppingCart) => {
              console.log('Product added to existing cart. Updated shopping cart:', shoppingCart);
            },
            (error: any) => {
              console.log("this cart id si");
              console.log(cartId);
              console.error('Error adding product to cart:', error);
            }
          );
        } else {
          // If the customer does not have a cart, create a new cart and add the product
          this.shoppingCartService.createCartFromSQL(this.loginService.getCustomerID()).subscribe(
            (newCart) => {
              this.customerService.updateCart(customerId, newCart.cartId).subscribe(

                (ret) => {
                  this.loginService.setCartID(newCart.cartId);
                  console.log("Sucessfully updated the Customer with the new Cart ID");
                },
                (error: any) => {
                  console.log(newCart.cartId);
                  console.log(customerId);
                  console.error("Couldnt update the Customer with the new ShoppingCart ID: ");
                }
              )
              // Now that a new cart is created, add the product to the new cart
              this.shoppingCartService.addToCartFromSQL(customerId, newCart.cartId, productId).subscribe(
                (shoppingCart) => {
                  console.log('Product added to new cart. Updated shopping cart:', shoppingCart);
                },
                (error: any) => {
                  console.error('Error adding product to cart:', error);
                }
              );

            },
            (error: any) => {
              console.log(this.loginService.getCustomerID());
              console.error('Error creating a new cart:', error);
            }
          );

        }
      } else {
        this.addToCartNoSQL(productId);
      }
    }
  }

  addToCartNoSQL(productId: number | undefined) {
    if (this.configService.useNoSQL == true) {
      const customerId = this.loginService.getCustomerIDFromNoSQL();
      // Check if the customer has an existing cart
      let cartId: number | null = this.loginService.getCartID();
      if (cartId == null) {
        console.info("Cart id should be null" + cartId);
      } else {
        console.info("Cart id is not null" + cartId);
      }

      if (cartId !== null) {
        // If the customer has an existing cart, add the product to the existing cart
        console.info(cartId);
        this.shoppingCartService.addToCartFromNoSQL(customerId!, productId!).subscribe(
          (shoppingCart) => {
            console.log('Product added to existing cart. Updated shopping cart:', shoppingCart);
          },
          (error: any) => {
            console.log("this cart id si");
            console.log(cartId);
            console.error('Error adding product to cart:', error);
          }
        );
      } else {
        // If the customer does not have a cart, create a new cart and add the product
        this.shoppingCartService.createCartFromNoSQL(customerId!).subscribe(
          (newCart) => {
            //setting a fake cart id 
            this.loginService.setCartID(1);
            // Now that a new cart is created, add the product to the new cart
            this.shoppingCartService.addToCartFromNoSQL(customerId!, productId!).subscribe(
              (shoppingCart) => {
                console.log('Product added to new cart. Updated shopping cart:', shoppingCart);
              },
              (error: any) => {
                console.error('Error adding product to cart:', error);
              }
            );

          },
          (error: any) => {
            console.log(this.loginService.getCustomerID());
            console.error('Error creating a new cart:', error);
          }
        );
      }
    }

  }

  updateAverageRating(averageRating: number) {
    // Do whatever you need to do with the average rating in your product component
    this.averageRating = averageRating;
    console.log('Received average rating:', averageRating);

  }
}