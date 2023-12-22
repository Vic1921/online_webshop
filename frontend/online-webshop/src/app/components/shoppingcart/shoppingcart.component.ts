import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { LoginService } from '../../services/login.service';
import { ShoppingCart } from '../../interfaces/shoppingcart';
import { Cartitem } from '../../interfaces/cartitem';
import { ProductDTO } from '../../interfaces/product';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shoppingcart',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './shoppingcart.component.html',
  styleUrl: './shoppingcart.component.css'
})
export class ShoppingcartComponent {
  private cartId: number | undefined;
  products: ProductDTO[] = [];
  cart: ShoppingCart;

  orderForm = new FormGroup({
    shippingDetails: new FormControl(''),
    paymentMethod: new FormControl(''),
    code: new FormControl(''),
    deliveryAddress: new FormControl(''),
  });

  constructor(
    private shoppingCartService: ShoppingcartService,
    public loginService: LoginService,
    private productService: ProductService,
    private orderService : OrderService,
    private router : Router
  ) {
    this.cart = { cartId: 0, totalPrice: 0, customerId: 0, cartItemDTOS: [] };

 
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
                this.cart.cartItemDTOS = cartItems;
                console.log(this.cart.cartItemDTOS);

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

  placeOrder() {
    if (this.loginService.isLoggedIn()) {
      const customerId = this.loginService.getCustomerID();
      const shoppingCartId = this.loginService.getCartID() ?? 0;
      const formValues = this.orderForm.value;
      const paymentMethod = formValues.paymentMethod ?? '';
      const shippingDetails = formValues.shippingDetails ?? '';  // Provide an empty string as the default value



      // Call the service to place the order
      this.orderService.placeOrder(customerId, shoppingCartId, paymentMethod, shippingDetails).subscribe(
        (response) => {
          console.log('Order placed successfully:', response);

          //redirect the customer to the order details component with the right order id
          const orderId = response.orderId;
          console.log(orderId);
          this.router.navigate(['/order', orderId]);
        },
        (error) => {
          console.log(customerId);
          console.log(shoppingCartId);
          console.log(paymentMethod);
          console.log(shippingDetails);

          console.error('Error placing order:', error);
        }
      );
    }
  }
  

  getItemsCount(): number {
    return this.cart?.cartItemDTOS?.length || 0;
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











