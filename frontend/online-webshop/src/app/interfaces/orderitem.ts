import { Order } from "./order";
import { Product } from "./product";

export interface OrderItem {
    orderItemId?: number,
    order: Order,
    product: Product;
    orderItemQuantity : number;
    
}
