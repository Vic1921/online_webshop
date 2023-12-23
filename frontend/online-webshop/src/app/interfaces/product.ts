export interface ProductDTO {
    productId: number; 
    productName: string;
    productDescription: string;
    productCategory: string;
    productPrice: number;
    productSKU: string;
    productQuantity: number;
    vendorName : string;
    productImageUrl: string;
    reviews: number[] 
}
