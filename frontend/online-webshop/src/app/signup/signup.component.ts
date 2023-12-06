import { Component, inject} from '@angular/core';
import { SignupService } from '../signup.service';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  signupForm = new FormGroup({
    name: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
  });

  signupService = inject(SignupService);

  onSubmit(): void {
    if (this.signupForm.valid) {
      const signupData = this.signupForm.value;
      
      this.signupService.signUp(signupData).subscribe(
        response => {
          console.log('Register successful!', response);
          // Redirect to login page or any other page
        },
        error => {
          console.error('Register failed', error);
        }
      );
    }
  }
}
