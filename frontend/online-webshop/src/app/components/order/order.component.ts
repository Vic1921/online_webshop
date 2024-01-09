import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { OrderService } from '../../services/order.service';
import { Order } from '../../interfaces/order';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { ConfigService } from '../../config.service';
import { OrderNoSQL } from '../../interfaces/ordernosql';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent {
  orders: (Order | OrderNoSQL)[] = [];
  constructor(private configService : ConfigService, private orderService : OrderService, private loginService : LoginService, private router : Router){
    if(this.configService.useNoSQL == false){
      const customerId = loginService.getCustomerID();
      this.orderService.getOrdersByCustomerIdFromSQL(customerId).subscribe(
        (response) => {
          this.orders = response;
          console.log('Successfully fetched order list', this.orders);
        },
        (error : any) => {
          console.log('Error fetching order list: ', error);
        }
      )
    }else{
      const customerId = loginService.getCustomerIDFromNoSQL();
      this.orderService.getOrdersByCustomerIdFromNoSQL(customerId!).subscribe(
        (response) => {
          this.orders = response;
          console.log('Successfully fetched order list', this.orders);
        },
        (error : any) => {
          console.log('Error fetching order list: ', error);
        }
      )
    }

    
  }

  getUseNoSQL() : boolean{
    return this.configService.useNoSQL;
  }

  navigateToOrderDetailsFromSQL(orderId : number) : void{
    this.router.navigate(['/order', orderId]);
  }

  
  navigateToOrderDetailsFromNoSQL(orderId : string) : void{
    this.router.navigate(['/order', orderId]);
  }

}
