import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { LoginService } from '../../services/login.service';
import { Product } from '../../product';
import { ShoppingCart } from '../../shoppingcart';

@Component({
  selector: 'app-shoppingcart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shoppingcart.component.html',
  styleUrl: './shoppingcart.component.css'
})
export class ShoppingcartComponent {
  private cartId: number | undefined;
  cart: ShoppingCart;
  constructor(private shoppingCartService: ShoppingcartService, public loginService: LoginService) {
    this.cart = { cartId: 0, totalPrice: 0, customerId: 0, products: [] };
    if (this.loginService.isLoggedIn()) {
      this.cartId = Number(this.loginService.getCartID());

      // Subscribe to getCart observable
      this.shoppingCartService.getCart(this.cartId).subscribe(
        (cart: ShoppingCart) => {
          this.cart = cart;

          // Now, this.cart contains the shopping cart details including product details
          // Fetch product details based on productIds
          this.shoppingCartService.getShoppingCartItems(cart.cartId).subscribe(
            (products: Product[]) => {
              this.cart.products = products;
            },
            (error) => {
              console.error('Error fetching product details:', error);
            }
          );
        },
        (error) => {
          console.error('Error fetching shopping cart:', error);
          // If the cart doesn't exist, create a new one
          this.shoppingCartService.createCart().subscribe(
            (newCart: ShoppingCart) => {
              this.cart = newCart;
              // Do something with the newly created shopping cart
            },
            (createError) => {
              console.error('Error creating shopping cart:', createError);
            }
          );
        }
      );
    }
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