import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Image } from 'src/app/common/image';
import { ProductInfo } from 'src/app/common/product-info';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.css']
})
export class ProductViewComponent implements OnInit {

  productInfo : ProductInfo;

  constructor(private productService : ProductService,
              private activatedRoute : ActivatedRoute) { }

  ngOnInit(): void {
    this.getProduct();
  }

  getProduct(){
    let productId : Number = +this.activatedRoute.snapshot.paramMap.get('id');
    this.productService.getProduct(productId).subscribe( productInfo => {
      this.productInfo = productInfo;
    });
  }

  sanitizeImgUrl(img : Image){
    return this.productService.sanitizeUrl(img);
  }

}
