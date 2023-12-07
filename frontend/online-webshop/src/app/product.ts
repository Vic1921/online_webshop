import { Vendor } from "./vendor";

export interface Product {
    productId: number; 
    productName: string;
    productDescription: string;
    productCategory: string;
    productPrice: number;
    productSKU: string;
    productQuantity: number;
    productImageUrl: string;
    vendor: number;
}
