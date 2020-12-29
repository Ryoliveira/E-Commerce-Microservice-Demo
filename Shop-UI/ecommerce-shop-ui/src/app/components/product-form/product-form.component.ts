import { Component, OnInit } from '@angular/core';
import { ProductInfo } from 'src/app/common/product-info';
import { ProductService } from 'src/app/services/product.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Product } from 'src/app/common/product';
import { ActivatedRoute, Router } from '@angular/router';
import { Stock } from 'src/app/common/stock';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
productInfoFormGroup : FormGroup;
imageFile : File = null;
imagePath : string = null;
imageFolder : string = "assets/images/products/";

constructor(private productService : ProductService, 
            private formBuilder : FormBuilder,
            private router: Router,
            private route : ActivatedRoute) {}

ngOnInit(): void {
  if(this.route.snapshot.paramMap.get('id')){
    console.log("I got an id");
    this.createEmptyForm();
    this.createUpdateForm();
  }else{
    console.log("No Id");
    this.createEmptyForm();
  }
}

// Getter for each form input
get productId() { return this.productInfoFormGroup.get('product.id');}
get name(){ return this.productInfoFormGroup.get('product.name');} 
get description(){ return this.productInfoFormGroup.get('product.description');}
get img() { return this.productInfoFormGroup.get('product.img');}
get price() { return this.productInfoFormGroup.get('product.price');}
get productStockId() { return this.productInfoFormGroup.get('stock.productId')}
get stock() { return this.productInfoFormGroup.get('stock.stock');}

createEmptyForm(){
  this.productInfoFormGroup = this.formBuilder.group({
    product: this.formBuilder.group({
      id: [undefined],
      name: [''],
      description: [''],
      img: [''],
      price: [0]
    }),

    stock: this.formBuilder.group({
      productId: [undefined],
      stock: [0]
    })
  })
}

createUpdateForm(){
  let productId : Number = +this.route.snapshot.paramMap.get('id');
  
  this.productService.getProduct(productId).subscribe(data => {
    let productInfo : ProductInfo = new ProductInfo(data.product, data.stock);

    //populate product portion of form
    this.productId.setValue(productInfo.product.id);
    this.name.setValue(productInfo.product.name);
    this.description.setValue(productInfo.product.description);
    this.price.setValue(productInfo.product.price);

    //populate stock portion of form
    this.productStockId.setValue(productInfo.stock.productId);
    this.stock.setValue(productInfo.stock.stock);

    console.log("Stock product id " + productInfo.stock.productId);

    // save product image path
    this.imagePath = productInfo.product.img;
  });
}

submitProduct(){
  let product : Product = this.productInfoFormGroup.controls["product"].value;
  let stock : Stock = this.productInfoFormGroup.controls["stock"].value;
  let productInfo : ProductInfo = new ProductInfo(product, stock);

  if(this.imageFile != null){
    productInfo.product.img = this.imageFolder + this.imageFile.name;
  }else{
    productInfo.product.img = this.imagePath;
  }
  
  console.log(productInfo);

  // if product id null, new product its attempting to be saved
  if(productInfo.product.id == null){
    this.productService.saveProduct(productInfo).subscribe(
      {
        next: response => {
          alert(`${response.product.name} has been added to inventory`);
        },
        error: err => {
          alert(`There was an error: ${err.message}`);
        }
      }
    );
  }else{
    this.productService.updateProduct(productInfo);
    console.log("Updated Product");
  }

  this.router.navigateByUrl('/products')
    .then(() => { 
      window.location.reload() 
    });

}

onImageSelected(event){
  console.log(event);
  this.imageFile = event.target.files[0];
  console.log(this.imageFolder + this.imageFile.name);
}

}
