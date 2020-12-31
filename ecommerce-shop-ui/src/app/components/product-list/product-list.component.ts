import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { ProductInfo } from 'src/app/common/product-info';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  productInfoList : ProductInfo[];


  constructor(private productService: ProductService,
              private route : Router) { }

  ngOnInit(): void {
    this.listProducts();
  }


  listProducts(){
    this.productService.getProductList().subscribe(
      data => {
        this.productInfoList = data;
      }
    )
  }

  deleteProduct(productId : Number){
    if(!confirm("are you sure you want to delete this item?")) return;

    this.productService.deleteProduct(productId);
    this.route.navigateByUrl("/products")
      .then(() => { 
        window.location.reload();
      });
  }

  updateProduct(id : Number){
    this.route.navigateByUrl(`/update-product/${id}`);
  }

  showProduct(id : Number){
    this.route.navigateByUrl(`/product-view/${id}`);
  }


}
