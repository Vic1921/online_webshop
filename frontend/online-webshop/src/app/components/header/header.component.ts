import { Component, Input, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { DbfillerService } from '../../services/dbfiller.service';
import { ProductService } from '../../services/product.service';
import { filter, switchMap, take } from 'rxjs/operators'; // Import switchMap and take from 'rxjs/operators'
import { ShoppingCart } from '../../interfaces/shoppingcart';
import { ShoppingcartService } from '../../services/shoppingcart.service';
import { MigrationNoSQLService } from '../../services/migrationnosql.service';
import { ConfigService } from '../../config.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent{
  private databaseFilled = false;
  @Input() isOrderComponent: boolean = false;
  @Input() isHomeComponent: boolean = false;
  @Input() isProductComponent: boolean = false;
  @Input() isProductDetailsComponent: boolean = false;


  cart : ShoppingCart | undefined;

  constructor(public configService : ConfigService, private router: Router, private activatedRouter : ActivatedRoute, private migrationService : MigrationNoSQLService,  public loginService : LoginService, private databaseFillerService: DbfillerService, private productService: ProductService, private shoppingCartService : ShoppingcartService) {
    console.log(configService.useNoSQL);

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        console.log('NavigationEnd event:', event);
        console.log('Current useNoSQL value:', configService.useNoSQL);
      }
    });

    if (this.loginService.isLoggedIn()) {
      const cartId = Number(this.loginService.getCartID());
      const customerId = this.loginService.getCustomerID();

      console.log(cartId);

      if(cartId != null && cartId != 0){
        this.shoppingCartService.getCart(cartId, customerId).subscribe(
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

  migrateDatabaseToNoSQL(): void {
    this.migrationService.migrateNoSQL().subscribe(
      response => {
        this.configService.setUseNoSQL(true);
        console.log(`useNoSQL = ${this.configService.useNoSQL}`);
        console.log("Successfully migrated: ", response);
      },
      error => {
        console.error("Error migrating: ", error);
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

  navigateToOrders(){
    this.router.navigate(['/orders']);
  }

  navigateToProduts() {
    this.router.navigate(['/products']);
  }

  navigateToShoppingCart(){
    this.router.navigate(['/shoppingcart']);
  }

  logout(){
      this.loginService.logout();
      localStorage.clear();
      this.router.navigate(['']);

  }
}