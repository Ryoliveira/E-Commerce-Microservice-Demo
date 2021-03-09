import { Category } from './category';
import { Image } from './image';
import { Product } from './product';
import { Stock } from './stock';

export class ProductInfo {

    product : Product;
    stock : Stock;
    category : Category;
    imgList : Image[];

    constructor(product: Product, stock: Stock, category: Category, images : Image[]){
        this.product = product;
        this.stock = stock;
        this.category = category;
        this.imgList = images;
    }

}
