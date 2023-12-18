import { Product } from "./product";

export interface ShoppingCart {
    cartId : number,
    totalPrice : number,
    customerId : number,
    products? : Product[],
}
