import { Category } from './category';
import { Product } from './product';
import { Stock } from './stock';

export class ProductInfo {

    product : Product;
    stock : Stock;
    category : Category;

    constructor(product: Product, stock: Stock, category: Category){
        this.product = product;
        this.stock = stock;
        this.category = category;
    }

}
