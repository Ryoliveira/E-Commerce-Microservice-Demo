import { Product } from './product';
import { Stock } from './stock';

export class ProductInfo {

    product : Product;
    stock : Stock;

    constructor(product: Product, stock: Stock){
        this.product = product;
        this.stock = stock;
    }

}
