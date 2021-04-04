import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Category } from 'src/app/common/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  categoryList : Category[] = [];


  constructor(private router : Router, private categoryService : CategoryService) { }

  ngOnInit(): void {
    this.populateCategories();
  }

  doSearch(searchQuery : string, searchCategoryId: string) : void {
    if(!searchQuery){
      alert("Search query must not be empty");
      return;
    }
    this.router.navigateByUrl("/").then(() => this.router.navigateByUrl(`/products/search/${searchCategoryId}/${searchQuery}`));
  }

  populateCategories(){
    this.categoryService.getCategoryList().subscribe(retrievedCategoryies => {
      this.categoryList = retrievedCategoryies;
    })
  }

}
