import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { DbfillerService } from '../../services/dbfiller.service';
import { ProductService } from '../../services/product.service';
import { switchMap, take } from 'rxjs/operators'; // Import switchMap and take from 'rxjs/operators'
import { ShoppingCart } from '../../interfaces/shoppingcart';
import { ShoppingcartService } from '../../services/shoppingcart.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  private databaseFilled = false;
  cart : ShoppingCart | undefined;

  constructor(private router: Router, public loginService : LoginService, private databaseFillerService: DbfillerService, private productService: ProductService, private shoppingCartService : ShoppingcartService) {
    if (this.loginService.isLoggedIn()) {
      const cartId = Number(this.loginService.getCartID());

      console.log(cartId);

      if(cartId != null){
        this.shoppingCartService.getCart(cartId).subscribe(
          (cart: ShoppingCart) => {
            this.cart = cart;
            console.log(this.cart); // Log inside the subscribe callback
          },
          (error) => {
            console.error('Error fetching shopping cart:', error);
          }
        );
      }
    }
  }

  getTotalItems() : number{
    const totalQuantity: number = this.cart?.cartItemDTOS?.reduce(
      (sum, cartItem) => sum + (cartItem.cartItemQuantity || 0),
      0) || 0;

      return totalQuantity;
  }


  fillDatabaseAndFetchProducts(): void {
    // Fill the database and fetch products only if not filled yet
    this.databaseFillerService.fillDatabase().pipe(
      switchMap(() => this.productService.getProductsFromHttp()),
      take(1) // Take only the first emitted value, i.e., complete after fetching products once
    ).subscribe(
      (data: any[]) => {
        console.log(data);
        this.productService.products = data.sort((a, b) => a.id - b.id);
        this.databaseFilled = true; // Set the flag to true after filling the database
      },
      (error: any) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  checkDatabaseAndFetchProducts(): void {
    if (!this.databaseFilled) {
      this.fillDatabaseAndFetchProducts();
    }
  }

  navigateToSignup() {
    this.router.navigate(['/signup']);
  }

  navigateToLogin(){
    this.router.navigate(['/login']);
  }

  navigateToProduts() {
    this.router.navigate(['/products']);
  }

  navigateToShoppingCart(){
    this.router.navigate(['/shoppingcart']);
  }

  logout(){
    this.loginService.logout();
  }
}