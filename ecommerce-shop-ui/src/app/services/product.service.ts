import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, pipe } from 'rxjs';
import { ProductInfo } from '../common/product-info';
import { catchError, map } from 'rxjs/operators';
import { Product } from '../common/product';
import { Stock } from '../common/stock';
import { Category } from '../common/category';
import { Image } from '../common/image';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiGatewayUrl = 'http://localhost:9020/api';
  private productListUrl = `${this.apiGatewayUrl}/inventory/products`;
  private productInfoUrl = `${this.apiGatewayUrl}/inventory/productInfo`;
  private deleteImageUrl = `${this.apiGatewayUrl}/i/images`;

  constructor(private httpClient: HttpClient, private domSanitizer : DomSanitizer) { }


  saveProduct( productInfo : ProductInfo, mainProductImage : File, imgFiles : File[]) : Observable<ProductInfo> {
    let formData = new FormData();
    formData.append("productInfo", JSON.stringify(productInfo));
    formData.append("mainProductImage", mainProductImage);
    for(let i = 0; i < imgFiles.length; i++){
      formData.append("additionalFiles[]", imgFiles[i]);
    }
    return this.httpClient.post<ProductInfo>(this.productInfoUrl, formData);
  }

  deleteProduct(productId : Number){
    let deleteProductUrl = `${this.productInfoUrl}/${productId}`;
    this.httpClient.delete(deleteProductUrl).subscribe(() => alert("Product has been deleted"));
  }

  deleteProductImages(imagesIdsToDelete : number[]){
    this.httpClient.put<string>(this.deleteImageUrl, imagesIdsToDelete).subscribe();
  }

  updateProduct(productInfo : ProductInfo, mainProductImageFile: File, additionalImageFiles : File[], imagesIdsToDelete : number[]) : Observable<ProductInfo> {
    let formData = new FormData();
    formData.append("productInfo", JSON.stringify(productInfo));
    formData.append("mainProductImage", mainProductImageFile);
    for(let i = 0; i < additionalImageFiles.length; i++){
      formData.append("additionalImageFiles[]", additionalImageFiles[i]);
    }
    this.deleteProductImages(imagesIdsToDelete);
    return this.httpClient.put<ProductInfo>(this.productInfoUrl, formData);
  }


  getProductList(): Observable<ProductInfo[]> {
    return this.httpClient.get<ProductListResponse>(this.productListUrl).pipe(
      map(response => response.productInfoList)
    );
  }

  getProduct(productId : Number) : Observable<ProductInfo> {
    let productUrl = `${this.productInfoUrl}/${productId}`;
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

  getProductsBySearchQuery(searchQuery: string, searchCategoryId: string) {
    let params = {"searchCategoryId" : searchCategoryId};
    let productsBySearchQueryUrl = `${this.productListUrl}/search/${searchQuery}`;
    return this.httpClient.get<ProductListResponse>(productsBySearchQueryUrl, {params : params}).pipe(
      map(reponse => reponse.productInfoList)
    );
  }

  sanitizeUrl(img : Image) : SafeUrl {
    return this.domSanitizer.bypassSecurityTrustUrl(`data:${img.fileType};base64, ${img.image}`);
  }
 

}


interface ProductListResponse {
  productInfoList: ProductInfo[]
}


interface ProductResponse {
  product : Product,
  stock : Stock,
  category: Category,
  mainProductImage : Image,
  imgList : Image[]
}


