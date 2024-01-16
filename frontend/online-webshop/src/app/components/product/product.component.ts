import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { ProductService } from '../../services/product.service';
import { switchMap } from 'rxjs';
import { DbfillerService } from '../../services/dbfiller.service';
import { ProductDTO } from '../../interfaces/product';
import { RouterModule } from '@angular/router';
import { ConfigService } from '../../config.service';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { LoginService } from '../../services/login.service';
import { CustomerService } from '../../services/customer.service';

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


  constructor(private customerService : CustomerService, private loginService : LoginService, private configService : ConfigService, private shoppingCartService : ShoppingcartService, private productService : ProductService){
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

 
  getProductImageStyle(imageUrl: string): object {
    return {
      'background-image': `url('./assets/images/products/${imageUrl}')`,
      'background-repeat': 'no-repeat',
      'background-size': 'contain',
      'background-position': 'center center'
    };
  }
  


}
