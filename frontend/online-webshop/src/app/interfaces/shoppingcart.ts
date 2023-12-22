import { Cartitem } from "./cartitem";
import { ProductDTO } from "./product";

export interface ShoppingCart {
    cartId : number,
    totalPrice : number,
    customerId : number,
    cartItemDTOS? : Cartitem[],
}
