import { Cartitem } from "./cartitem";
import { Product } from "./product";

export interface ShoppingCart {
    cartId : number,
    totalPrice : number,
    customerId : number,
    products? : Cartitem[],
}
