import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SignupComponent } from './signup/signup.component';
import { ProductComponent } from './product/product.component';
import { ShoppingcartComponent } from './shoppingcart/shoppingcart.component';
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
    }
];
