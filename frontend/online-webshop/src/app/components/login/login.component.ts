import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators, FormGroup} from '@angular/forms';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });


  constructor (private loginService : LoginService) {}

  onSubmit(): void {
    if(this.loginForm.valid){
      const loginData = this.loginForm.value;

      this.loginService.login(loginData).subscribe(
        response => {
          console.log('Login successful!', response);
          // Redirect to login page or any other page
        },
        error => {
          console.error('Register failed', error);
        }
      );
    }
  }
}
