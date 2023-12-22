export interface ProductDTO {
    productId: number; 
    productName: string;
    productDescription: string;
    productCategory: string;
    productPrice: number;
    productSKU: string;
    productQuantity: number;
    productImageUrl: string;
    reviews: number[] 
}
