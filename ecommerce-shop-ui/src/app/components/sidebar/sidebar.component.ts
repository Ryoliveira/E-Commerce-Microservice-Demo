import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Category } from 'src/app/common/category';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  categories : Category[];

  constructor(private productService: ProductService,
              private route : Router) { }

  ngOnInit(): void {
    this.populateSideBar();
  }

  populateSideBar(){
    this.productService.getProductCategories().subscribe(categories => {
      this.categories = categories;
    })
  }

  redirectToProductList(categoryId : Number){
    this.route.navigateByUrl('/').then(() => this.route.navigateByUrl(`/products/${categoryId}`));
  }

}
