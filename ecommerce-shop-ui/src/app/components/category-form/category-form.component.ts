import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Category } from 'src/app/common/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit {

  categoryInfoFormGroup : FormGroup;

  constructor(private categoryService : CategoryService,
              private formBuilder : FormBuilder,
              private route : Router) { }

  ngOnInit(): void {
    this.createForm();
  }


  createForm() : void {
    this.categoryInfoFormGroup = this.formBuilder.group({
      category : this.formBuilder.group({
        id:[undefined],
        name:['']
      })
    })
  }

  submitCategoryName() : void {
    let category : Category = this.categoryInfoFormGroup.controls["category"].value;
    this.categoryService.isCategoryNamePresent(category.name).subscribe(isNameNotValid => {
      if(!isNameNotValid){ 
        console.log(category.name);
        console.log(`Boolean : ${isNameNotValid}`)
        this.categoryService.saveCategory(category).subscribe({
          next : response => {
            alert(`${response.name} has been saved`);
          },
          error : err => {
            alert(`Error: ${err.message}`);
          }
        });
        this.route.navigateByUrl("/products");
      }else{
        alert(`${category.name} is already a category, please enter a new category name`);
      }
    });
  }

}
