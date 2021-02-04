import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, pipe } from 'rxjs';
import { ProductInfo } from '../common/product-info';
import { catchError, map } from 'rxjs/operators';
import { Product } from '../common/product';
import { Stock } from '../common/stock';
import { Category } from '../common/category';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiGatewayUrl = 'http://localhost:9020/api';
  private productListUrl = `${this.apiGatewayUrl}/inventory/products`;
  private ProductInfoUrl = `${this.apiGatewayUrl}/inventory/productInfo`;


  constructor(private httpClient: HttpClient) { }


  saveProduct( productInfo : ProductInfo) : Observable<any> {
    return this.httpClient.post<ProductInfo>(this.ProductInfoUrl, productInfo);
  }

  deleteProduct(productId : Number){
    let deleteProducturl = `${this.ProductInfoUrl}/${productId}`;
    this.httpClient.delete(deleteProducturl).subscribe(() => alert("Product has been deleted"));
  }

  updateProduct(productInfo : ProductInfo) {
   this.httpClient.put(this.ProductInfoUrl, productInfo).subscribe(() => alert(`${productInfo.product.name} has been updated`));
  }


  getProductList(): Observable<ProductInfo[]> {
    return this.httpClient.get<ProductListResponse>(this.productListUrl).pipe(
      map(response => response.productInfoList)
    );
  }

  getProduct(productId : Number) : Observable<ProductInfo> {
    let productUrl = `${this.ProductInfoUrl}/${productId}`;
    return this.httpClient.get<ProductResponse>(productUrl).pipe(
      map(response => response)
    );
  }

  getProductsByCategory(categoryId : Number) : Observable<ProductInfo[]> {
    let productsByCategoryUrl = `${this.productListUrl}/${categoryId}`;
    return this.httpClient.get<ProductListResponse>(productsByCategoryUrl).pipe(
      map(response => response.productInfoList)
    );
  }

 

}


interface ProductListResponse {
  productInfoList: ProductInfo[]
}


interface ProductResponse {
  product : Product,
  stock : Stock,
  category: Category
}


