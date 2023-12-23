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

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent {
  product: ProductDTO | undefined;

  constructor(private route: ActivatedRoute, private productService: ProductService, private loginService: LoginService, private shoppingCartService: ShoppingcartService, private customerService : CustomerService) {
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

  searchArticle() {
    throw new Error('Method not implemented.');
  }
  addToCart(productId: number | undefined) : void {
    if (productId == undefined) {
      console.error('Product Id is undefined');
      return;
    }

    if (this.loginService.isLoggedIn()) {
      const customerId = this.loginService.getCustomerID();
      // Check if the customer has an existing cart
      let cartId: number | null = this.loginService.getCartID();
      if(cartId == null) {
        console.info("Cart id should be null" + cartId);
      }else{
        console.info("Cart id is not null" + cartId);

      }

      if (cartId !== null) {
        // If the customer has an existing cart, add the product to the existing cart
        console.info(cartId);
        this.shoppingCartService.addToCart(customerId, cartId, productId).subscribe(
          (shoppingCart) => {
            // Handle the success response
            console.log('Product added to existing cart. Updated shopping cart:', shoppingCart);
          },
          (error: any) => {
            // Handle the error response
            console.log("this cart id si");
            console.log(cartId);
            console.error('Error adding product to cart:', error);
          }
        );
      } else {
        // If the customer does not have a cart, create a new cart and add the product

        this.shoppingCartService.createCart(this.loginService.getCustomerID()).subscribe(
          (newCart) => {
            this.customerService.updateCart(customerId, newCart.cartId).subscribe(
           
              (ret) =>{
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
            
            this.shoppingCartService.addToCart(customerId, newCart.cartId, productId).subscribe(
              (shoppingCart) => {
                // Handle the success response
                console.log('Product added to new cart. Updated shopping cart:', shoppingCart);
              },
              (error: any) => {
                // Handle the error response
                console.error('Error adding product to cart:', error);
              }
            );
            
          },
          (error: any) => {
            console.log(this.loginService.getCustomerID());
            // Handle the error response when creating a new cart
            console.error('Error creating a new cart:', error);
          }
        );
        
      }
    } else {
      // Handle the case when the user is not logged in (optional)
      console.log('User is not logged in. Please log in to add items to the cart.');
    }
  }
}