import { Component, inject } from '@angular/core';
import { SignupService } from '../../services/signup.service';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { Router } from '@angular/router';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FooterComponent, HeaderComponent],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  signupForm = new FormGroup({
    name: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    street: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    postalCode: new FormControl('', Validators.required),
    country: new FormControl('', Validators.required),
  });

  constructor(private signupService: SignupService, private router: Router) { }

  onSubmit(): void {
    if (this.signupForm.valid) {
      const signupData = this.signupForm.value;

      this.signupService.signUp(signupData).subscribe(
        response => {
          console.log('Register successful!', response);
          // Redirect to login page or any other page
          this.router.navigate(['']);
        },
        error => {
          console.error('Register failed', error);
        }
      );
    }
  }
}
