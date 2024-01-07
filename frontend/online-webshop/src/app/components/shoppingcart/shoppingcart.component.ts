import { ChangeDetectorRef, Component } from '@angular/core';
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
import { CustomerService } from '../../services/customer.service';
import { ConfigService } from '../../config.service';
import { ObjectId } from 'bson';

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
    private configService: ConfigService,
    private orderService: OrderService,
    private customerService: CustomerService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {
    this.cart = { cartId: 0, totalPrice: 0, customerId: 0, cartItemDTOS: [] };


    if (this.loginService.isLoggedIn()) {
      this.cartId = Number(this.loginService.getCartID());

      // Subscribe to getCart observable
      if (this.cartId != null && configService.useNoSQL == false) {
        let customerId = this.loginService.getCustomerID();
        this.shoppingCartService.getCart(this.cartId, customerId!).subscribe(
          (cart: ShoppingCart) => {
            this.cart = cart;
            console.log(cart);

            // Now, this.cart contains the shopping cart details including product details
            // Fetch product details based on productIds
            this.shoppingCartService.getShoppingCartItems(cart.cartId, customerId!).subscribe(
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
      } else {
        this.getCartByNoSQL();
      }
    }
  }

  getCartByNoSQL() {
    const customerId = this.loginService.getCustomerIDFromNoSQL();
    this.shoppingCartService.getCartNoSQL(customerId!).subscribe(
      (cart: ShoppingCart) => {
        this.cart = cart;
        console.log(cart);

        // Now, this.cart contains the shopping cart details including product details
        // Fetch product details based on productIds
        this.shoppingCartService.getShoppingCartItemsNoSQL(customerId!).subscribe(
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

  convertToObjectId(id : number): string {
    const objectId = new ObjectId(id.toString());
    return objectId.toHexString();
  }

  addToCart(productId: number | undefined): void {
    if (productId == undefined) {
      console.error('Product Id is undefined');
      return;
    }

    if (this.loginService.isLoggedIn()) {
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
        this.shoppingCartService.addToCart(customerId, cartId, productId).subscribe(
          (shoppingCart) => {
            // Handle the success response
            this.cart = shoppingCart; // Updated shopping cart
            this.cdr.detectChanges();

            console.log('Product added to existing cart. Updated shopping cart:', shoppingCart);
          },
          (error: any) => {
            // Handle the error response
            console.log("this cart id si");
            console.log(cartId);
            console.error('Error adding product to cart:', error);
          }
        );
      }
    }
  }

  placeOrder() {
    if (this.configService.useNoSQL == true) {
      if (this.loginService.isLoggedIn()) {
        const customerId = this.loginService.getCustomerID();
        const shoppingCartId = this.loginService.getCartID() ?? 0;
        const formValues = this.orderForm.value;
        const paymentMethod = formValues.paymentMethod ?? '';
        const shippingDetails = formValues.shippingDetails ?? '';  // Provide an empty string as the default value



        // Call the service to place the order
        this.orderService.placeOrderFromSQL(customerId, shoppingCartId, paymentMethod, shippingDetails).subscribe(
          (response) => {
            console.log('Order placed successfully:', response);
            //redirect the customer to the order details component with the right order id
            const orderId = response.orderId;
            //delete the cart
            this.loginService.setCartID(null);
            console.log(orderId);
            console.log(paymentMethod);
            this.router.navigate(['/order', orderId, { paymentMethod: paymentMethod }]);

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
    }else{
      this.placeOrderNoSQL();
    }
  }

  placeOrderNoSQL() {
    if (this.loginService.isLoggedIn()) {
      const customerId = this.loginService.getCustomerIDFromNoSQL();
      const formValues = this.orderForm.value;
      const paymentMethod = formValues.paymentMethod ?? '';
      const shippingDetails = formValues.shippingDetails ?? '';  // Provide an empty string as the default value

      // Call the service to place the order
      this.orderService.placeOrderFromNoSQL(customerId!, paymentMethod, shippingDetails).subscribe(
        (response) => {
          console.log('Order placed successfully:', response);
          //redirect the customer to the order details component with the right order id
          const orderId = this.convertToObjectId(response.orderId);
          //delete the cart
          this.loginService.setCartID(null);
          console.log(orderId);
          console.log(paymentMethod);
          this.router.navigate(['/order', orderId, { paymentMethod: paymentMethod }]);

        },
        (error) => {
          console.log(customerId);
          console.log(paymentMethod);
          console.log(shippingDetails);

          console.error('Error placing order:', error);
        }
      );
    }
  }

  getTotalItems(): number {
    const totalQuantity: number = this.cart?.cartItemDTOS?.reduce(
      (sum, cartItem) => sum + (cartItem.cartItemQuantity || 0),
      0) || 0;

    return totalQuantity;
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











