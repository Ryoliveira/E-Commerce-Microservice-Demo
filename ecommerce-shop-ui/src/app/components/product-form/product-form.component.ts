import { Component, OnInit } from '@angular/core';
import { ProductInfo } from 'src/app/common/product-info';
import { ProductService } from 'src/app/services/product.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Product } from 'src/app/common/product';
import { ActivatedRoute, Router } from '@angular/router';
import { Stock } from 'src/app/common/stock';
import { Category } from 'src/app/common/category';
import { CategoryService } from 'src/app/services/category.service';
import { Image } from 'src/app/common/image';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
productInfoFormGroup : FormGroup;
imageFile : File = null;

categories : Category[];

constructor(private productService : ProductService,
            private categoryService : CategoryService, 
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
get price() { return this.productInfoFormGroup.get('product.price');}
get productStockId() { return this.productInfoFormGroup.get('stock.productId')}
get stock() { return this.productInfoFormGroup.get('stock.stock');}
get categoryId() { return this.productInfoFormGroup.get("category.categoryId");}
get categoryName() { return this.productInfoFormGroup.get("category.name");}

createEmptyForm() : void {
  this.productInfoFormGroup = this.formBuilder.group({
    product: this.formBuilder.group({
      id: [undefined],
      name: [''],
      description: [''],
      categoryId: [0],
      price: [0]
    }),

    stock: this.formBuilder.group({
      productId: [undefined],
      stock: [0]
    }),

    category : this.formBuilder.group({
      categoryId: [undefined],
      name : ['']
    }),
  })
}

createUpdateForm() : void {
  let productId : number = +this.route.snapshot.paramMap.get('id');
  
  this.productService.getProduct(productId).subscribe(data => {
    let productInfo : ProductInfo = new ProductInfo(data.product, data.stock, data.category, data.img);

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

    console.log("Product Category Id: " + productInfo.category.categoryId);
    console.log("Stock product id " + productInfo.stock.productId);
  });
}

submitProduct() : void {
  let product : Product = this.productInfoFormGroup.controls["product"].value;
  let stock : Stock = this.productInfoFormGroup.controls["stock"].value;
  let category : Category = this.productInfoFormGroup.controls["category"].value;
  // product info will get a null image since it is sent seperatly 
  let productInfo : ProductInfo = new ProductInfo(product, stock, category, new Image());

  // This is a temporary fix
  if(productInfo.product.categoryId == 0){
    productInfo.product.categoryId = category.categoryId;
  }
  
  console.log(productInfo);

  // if product id null, new product is attempting to be saved
  if(productInfo.product.id == null){
    this.productService.saveProduct(productInfo).subscribe(productInfo => {
      this.productService.saveImg(productInfo.product.id, this.imageFile).subscribe();
      this.redirectToProductsPage();
    });
  }else{
    this.productService.updateProduct(productInfo);
    if(this.imageFile != null){
      this.productService.updateImage(productInfo.product.id, this.imageFile).subscribe();
    }
    this.redirectToProductsPage();
  }
}

populateCategories() : void {
  this.categoryService.getCategoryList().subscribe(data => {
    this.categories = data;
  })
}

onImageSelected(event) : void {
  console.log(event);
  this.imageFile = event.target.files[0];
  console.log(this.imageFile);
}

onCategoryChange(categoryId : number) : void {
  this.productCategoryId.setValue(categoryId);
  console.log(categoryId);
  console.log("-");
}

redirectToProductsPage(){
  this.router.navigateByUrl('/products')
  .then(() => { 
    window.location.reload() 
  });
}

}
