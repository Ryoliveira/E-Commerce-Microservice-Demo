import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  constructor(private router : Router) { }

  ngOnInit(): void {
  }

  doSearch(searchQuery : string) : void {
    console.log(`Seach Query: ${searchQuery}`);
    this.router.navigateByUrl("/").then(() => this.router.navigateByUrl(`/products/search/${searchQuery}`));
  }

}
