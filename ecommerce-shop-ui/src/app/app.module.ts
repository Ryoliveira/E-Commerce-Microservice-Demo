import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { HttpClientModule } from '@angular/common/http'
import { ProductService } from './services/product.service';

import { Routes, RouterModule} from '@angular/router';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { ProductViewComponent } from './components/product-view/product-view.component';

const routes: Routes = [
  {path: 'home', component: HomepageComponent},
  {path: 'product-view/:id', component: ProductViewComponent},
  {path: 'update-product/:id', component: ProductFormComponent},
  {path: 'add-product', component: ProductFormComponent},
  {path: 'products', component: ProductListComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: '**', redirectTo: '/home', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    ProductFormComponent,
    ProductFormComponent,
    NavbarComponent,
    HomepageComponent,
    ProductViewComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [ProductService],
  bootstrap: [AppComponent]
})
export class AppModule { }
