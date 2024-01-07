import { OrderItem } from "./orderitem";

export interface OrderNoSQL {
    orderId : string;
    orderDate: string;
    orderTotalMount: number;
    customerId: number;
    orderItems: OrderItem[];
}
