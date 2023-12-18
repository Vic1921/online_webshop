import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  loading = false;
  errorText: string | null = null;


  constructor(private loginService: LoginService, private router: Router) { }

  onSubmit(): void {
    if (this.loginForm.valid && !this.loading) {
      this.loading = true;
      this.errorText = null;

      const loginData = this.loginForm.value;

      this.loginService.login(loginData).subscribe(
        response => {
          //navigate to the homepage
          this.router.navigate(['']);
          console.log('Login successful!', response);
          // Redirect to login page or any other page
        },
        error => {
          console.error('Login failed', error);
          this.errorText = 'Login failed. Please check your email and password.';
        }
      ).add(() => {
        this.loading = false; // Reset loading indicator regardless of success or failure
      });
    }
  }

  navigateToSignup(){
    this.router.navigate(['/signup']);
  }
}
