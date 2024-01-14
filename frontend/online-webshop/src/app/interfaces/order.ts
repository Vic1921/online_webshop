import { OrderItem } from "./orderitem";

export interface Order {
    orderId : number;
    orderDate: string;
    orderTotalMount: number;
    customerId: number;
    orderPayment: string;
    orderShippingDetails: string;
    orderItems: OrderItem[];
}
