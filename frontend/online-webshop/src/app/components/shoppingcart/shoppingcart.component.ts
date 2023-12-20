import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { LoginService } from '../../services/login.service';
import { ShoppingCart } from '../../shoppingcart';
import { Cartitem } from '../../cartitem';
import { Product } from '../../product';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-shoppingcart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shoppingcart.component.html',
  styleUrl: './shoppingcart.component.css'
})
export class ShoppingcartComponent {
  private cartId: number | undefined;
  products: Product[] = [];
  cart: ShoppingCart;
  constructor(
    private shoppingCartService: ShoppingcartService,
    public loginService: LoginService,
    private productService: ProductService
  ) {
    this.cart = { cartId: 0, totalPrice: 0, customerId: 0, products: [] };
    if (this.loginService.isLoggedIn()) {
      this.cartId = Number(this.loginService.getCartID());

      // Subscribe to getCart observable
      this.shoppingCartService.getCart(this.cartId).subscribe(
        (cart: ShoppingCart) => {
          this.cart = cart;
          console.log(cart);

          // Now, this.cart contains the shopping cart details including product details
          // Fetch product details based on productIds
          this.shoppingCartService.getShoppingCartItems(cart.cartId).subscribe(
            (cartItems: Cartitem[] | null) => {
              if (cartItems !== null) {
                const productIds = cartItems.map((item) => item.productId);
                this.cart.products = cartItems;
                console.log(this.cart.products);

                // Iterate through productIds and fetch products
                productIds.forEach((productId) => {
                  this.productService.getProduct(productId).subscribe(
                    (product) => {
                      // Push the fetched product to the products array
                      this.products.push(product);

                      // Now, this.cart contains the shopping cart details including products
                    },
                    (error) => {
                      console.error(`Error fetching product with ID ${productId}:`, error);
                    }
                  );
                });


              } else {
                console.log(this.cart);
                console.error('Cart items are null.');
              }
            },
            (error) => {
              console.error('Error fetching shopping cart items:', error);
            }
          );
        },
        (error) => {
          console.error('Error fetching shopping cart:', error);
        }
      );
    }
  }

  getItemsCount(): number {
    return this.cart?.products?.length || 0;
  }
  
  getProductImageStyle(imageUrl: string): object {
    return {
      'background-image': `url('./assets/images/products/'${imageUrl}')`,
      'background-repeat': 'no-repeat',
      'background-size': 'cover',
      'background-position': 'center center'
    };
  }
}











