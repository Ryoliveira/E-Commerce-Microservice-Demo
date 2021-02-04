import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Category } from '../common/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiGatewayUrl = 'http://localhost:9020/api';
  private categoriesUrl = `${this.apiGatewayUrl}/c/categories`;
  private categoryUrl = `${this.apiGatewayUrl}/c/category`;
  private categoryHasNameUrl = `${this.categoryUrl}/name`;

  constructor(private httpClient: HttpClient) { }


  getCategoryList() : Observable<Category[]> {
    return this.httpClient.get<CategoryListResponse>(this.categoriesUrl).pipe(
      map(response => response.categoryList)
    );
  }


  saveCategory(category : Category) : Observable<Category> {
    console.log(this.categoryUrl);
    console.log(category);
    return this.httpClient.post<Category>(this.categoryUrl, category);
  }

  isCategoryNamePresent(categroyName : String) : boolean {
    return this.httpClient.get<boolean>(this.categoryHasNameUrl) ? true : false;
  }

}


interface CategoryListResponse {
  categoryList : Category[]
}