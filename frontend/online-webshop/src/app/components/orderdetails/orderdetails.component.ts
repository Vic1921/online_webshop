import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Order } from '../../interfaces/order';
import { OrderService } from '../../services/order.service';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { LoginService } from '../../services/login.service';
import { CustomerService } from '../../services/customer.service';
import { ConfigService } from '../../config.service';


@Component({
  selector: 'app-orderdetails',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './orderdetails.component.html',
  styleUrl: './orderdetails.component.css'
})
export class OrderdetailsComponent {
  order : Order | undefined;
  private orderTotal: number = 0;
  paymentMethod!: string | null;

  constructor(private configService : ConfigService, private orderService : OrderService, private route : ActivatedRoute, private loginService : LoginService){
    if(configService.useNoSQL == false){
      this.getOrderSQL();
    }else{
      this.getOrderNoSQL();
    }
  }

  getOrderSQL(){
    const orderId = Number(this.route.snapshot.paramMap.get('orderId'));
    this.orderService.getOrderByIDFromSQL(orderId).subscribe(
      (response) => {
        console.log('Order successfully fetched:', response);
        this.order = response;
        this.order.orderItems = response.orderItems;
        this.paymentMethod = this.route.snapshot.paramMap.get('paymentMethod');
            
        console.log(this.paymentMethod);
        this.calculateOrderTotal();

        console.log(this.order);
      },
      (error : any) =>{
        console.error('Error fetching order:', error);
      }
    )
  }

  getOrderNoSQL(){
    const orderId = this.route.snapshot.paramMap.get('orderId');
    this.orderService.getOrderByIDFromNoSQL(orderId!).subscribe(
      (response) => {
        console.log('Order successfully fetched:', response);
        this.order = response;
        this.order.orderItems = response.orderItems;
        this.paymentMethod = this.route.snapshot.paramMap.get('paymentMethod');
            
        console.log(this.paymentMethod);
        this.calculateOrderTotal();

        console.log(this.order);
      },
      (error : any) =>{
        console.error('Error fetching order:', error);
      }
    )
  }

  private calculateOrderTotal(): void {
    if (this.order) {
      this.orderTotal = this.order.orderItems.reduce((total, orderItem) => {
        return total + orderItem.orderItemQuantity * orderItem.productDTO.productPrice;
      }, 0);
    }
  }

  public getOrderTotal(): number {
    return this.orderTotal;
  }

  public getCustomerName() : string | null {
    return this.loginService.getCustomerName();
  }

  public getCustomerAddress() : string | null {
    return this.loginService.getCustomerAddress();
  }
}
