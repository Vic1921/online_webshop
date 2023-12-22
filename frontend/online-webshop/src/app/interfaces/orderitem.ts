import { Order } from "./order";
import { ProductDTO } from "./product";

export interface OrderItem {
    orderItemId?: number,
    order: Order,
    productDTO: ProductDTO;
    orderItemQuantity : number;
    
}
