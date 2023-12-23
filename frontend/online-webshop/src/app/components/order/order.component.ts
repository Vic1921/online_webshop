import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { OrderService } from '../../services/order.service';
import { Order } from '../../interfaces/order';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent {
  orders : Order[] = [];

  constructor(private orderService : OrderService, private loginService : LoginService, private router : Router){
    const customerId = loginService.getCustomerID();
    this.orderService.getOrdersByCustomerId(customerId).subscribe(
      (repsonse) => {
        this.orders = repsonse;
        console.log('Successfully fetched order list', this.orders);
      },
      (error : any) => {
        console.log('Error fetching order list: ', error);
      }
    )
    
  }

  navigateToOrderDetails(orderId : number) : void{
    this.router.navigate(['/order', orderId]);
  }

}
