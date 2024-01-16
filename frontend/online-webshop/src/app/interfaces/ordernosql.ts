import { OrderItem } from "./orderitem";

export interface OrderNoSQL {
    orderId : string;
    orderDate: string;
    orderTotalMount: number;
    orderPayment: string;
    orderShippingDetails: string;
    customerId: number;
    orderItems: OrderItem[];
}
