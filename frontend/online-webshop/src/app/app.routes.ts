import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SignupComponent } from './signup/signup.component';
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
];
