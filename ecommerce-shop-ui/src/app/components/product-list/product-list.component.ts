import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { ProductInfo } from 'src/app/common/product-info';
import { ActivatedRoute, Router } from '@angular/router';
import { SafeUrl } from '@angular/platform-browser';
import { Image } from '../../common/image';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  productInfoList : ProductInfo[] = [];


  constructor(private productService: ProductService,
              private route : Router,
              private activeRoute : ActivatedRoute) { }

  ngOnInit(): void {
    if(this.activeRoute.snapshot.paramMap.get("categoryId")){
      this.listProductsByCategory();
    }
    else if(this.activeRoute.snapshot.paramMap.get("searchQuery")){
      this.listProductsBySearchQuery();
    }
    else{
      this.listProducts();
    }
  }
  listProductsByCategory() {
    let categoryId: Number = +this.activeRoute.snapshot.paramMap.get("categoryId");
    this.productService.getProductsByCategory(categoryId).subscribe(
      products => {
        this.productInfoList = products;
    })
  }


  listProducts(){
    this.productService.getProductList().subscribe(
      data => {
        this.productInfoList = data;
      }
    )
  }

  listProductsBySearchQuery(){
    let searchQuery = this.activeRoute.snapshot.paramMap.get("searchQuery");
    let searchCategoryId = this.activeRoute.snapshot.paramMap.get("searchCategoryId");
    this.productService.getProductsBySearchQuery(searchQuery, searchCategoryId).subscribe(
      data => {
        this.productInfoList = data;
      }
    );
  }

  deleteProduct(productId : Number){
    if(!confirm("are you sure you want to delete this item?")) return;

    this.productService.deleteProduct(productId);
    window.location.reload();
  }

  updateProduct(id : Number){
    this.route.navigateByUrl(`/update-product/${id}`);
  }

  showProduct(id : Number){
    this.route.navigateByUrl(`/product-view/${id}`);
  }

  sanitizeImgUrl(img : Image) : SafeUrl {
    return this.productService.sanitizeUrl(img);
  }


}
