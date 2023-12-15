import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(private router: Router) {}

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
}