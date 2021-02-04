import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Category } from 'src/app/common/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  categories : Category[];

  constructor(private categoryService: CategoryService,
              private route : Router) { }

  ngOnInit(): void {
    this.populateSideBar();
  }

  populateSideBar(){
    this.categoryService.getCategoryList().subscribe(categories => {
      this.categories = categories;
    })
  }

  redirectToProductList(categoryId : Number){
    this.route.navigateByUrl('/').then(() => this.route.navigateByUrl(`/products/${categoryId}`));
  }

}
