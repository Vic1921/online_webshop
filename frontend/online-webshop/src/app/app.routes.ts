import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SignupComponent } from './components/signup/signup.component';
import { ProductComponent } from './components/product/product.component';
import { ShoppingcartComponent } from './components/shoppingcart/shoppingcart.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { LoginComponent } from './components/login/login.component';
export const routes: Routes = [
    {
        path:'',
        component: HomeComponent,
        title: 'Home page',
    },
    {
        path: 'signup',
        component: SignupComponent,
        title: 'Signup',
    },
    {
        path: 'products',
        component: ProductComponent,
        title: 'Products'
    },
    {
        path:  'shoppingcart',
        component: ShoppingcartComponent,
        title: 'Shoppingcart'
    },
    {
        path:'products/:id',
        component: ProductDetailsComponent,
        title: 'Product'
    }, 
    {
        path: 'login',
        component: LoginComponent,
        title: 'Login',
    }
];
