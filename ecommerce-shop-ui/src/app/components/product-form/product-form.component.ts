import { Component, OnInit } from '@angular/core';
import { ProductInfo } from 'src/app/common/product-info';
import { ProductService } from 'src/app/services/product.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Product } from 'src/app/common/product';
import { ActivatedRoute, Router } from '@angular/router';
import { Stock } from 'src/app/common/stock';
import { Category } from 'src/app/common/category';

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

categories : Category[];

constructor(private productService : ProductService, 
            private formBuilder : FormBuilder,
            private router: Router,
            private route : ActivatedRoute) {}

ngOnInit(): void {
  this.createEmptyForm();
  this.populateCategories();
  console.log(this.categories);
  if(this.route.snapshot.paramMap.get('id')){
    this.createUpdateForm();  
  }
}

// Getter for each form input
get productId() { return this.productInfoFormGroup.get('product.id');}
get name(){ return this.productInfoFormGroup.get('product.name');} 
get description(){ return this.productInfoFormGroup.get('product.description');}
get productCategoryId() { return this.productInfoFormGroup.get("product.categoryId");}
get img() { return this.productInfoFormGroup.get('product.img');}
get price() { return this.productInfoFormGroup.get('product.price');}
get productStockId() { return this.productInfoFormGroup.get('stock.productId')}
get stock() { return this.productInfoFormGroup.get('stock.stock');}
get categoryId() { return this.productInfoFormGroup.get("category.categoryId");}
get categoryName() { return this.productInfoFormGroup.get("category.name");}

createEmptyForm(){
  this.productInfoFormGroup = this.formBuilder.group({
    product: this.formBuilder.group({
      id: [undefined],
      name: [''],
      description: [''],
      categoryId: [0],
      img: [''],
      price: [0]
    }),

    stock: this.formBuilder.group({
      productId: [undefined],
      stock: [0]
    }),

    category : this.formBuilder.group({
      categoryId: [undefined],
      name : ['']
    })
  })
}

createUpdateForm(){
  let productId : Number = +this.route.snapshot.paramMap.get('id');
  
  this.productService.getProduct(productId).subscribe(data => {
    let productInfo : ProductInfo = new ProductInfo(data.product, data.stock, data.category);

    //populate product portion of form
    this.productId.setValue(productInfo.product.id);
    this.name.setValue(productInfo.product.name);
    this.description.setValue(productInfo.product.description);
    this.price.setValue(productInfo.product.price);

    //populate stock portion of form
    this.productStockId.setValue(productInfo.stock.productId);
    this.stock.setValue(productInfo.stock.stock);

    //populate category portion of form
    this.categoryId.setValue(productInfo.category.categoryId);
    this.categoryName.setValue(productInfo.category.name);

    console.log("Stock product id " + productInfo.stock.productId);

    // save product image path
    this.imagePath = productInfo.product.img;
  });
}

submitProduct(){
  let product : Product = this.productInfoFormGroup.controls["product"].value;
  let stock : Stock = this.productInfoFormGroup.controls["stock"].value;
  let category : Category = this.productInfoFormGroup.controls['category'].value;
  let productInfo : ProductInfo = new ProductInfo(product, stock, category);

  console.log(productInfo);

  if(this.imageFile != null){
    productInfo.product.img = this.imageFolder + this.imageFile.name;
  }else{
    productInfo.product.img = this.imagePath;
  }
  
  console.log(productInfo);

  // if product id null, new product is attempting to be saved
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

populateCategories(){
  this.productService.getProductCategories().subscribe(data => {
    this.categories = data;
  })
}

onImageSelected(event){
  console.log(event);
  this.imageFile = event.target.files[0];
  console.log(this.imageFolder + this.imageFile.name);
}

onCategoryChange(event){
  let id : Number = +event.target.value[0];
  this.productCategoryId.setValue(id);
  console.log(id);
}

}
