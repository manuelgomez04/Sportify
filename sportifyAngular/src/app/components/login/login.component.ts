import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginForm: FormGroup;
  showPassword = false;

  constructor(private fb: FormBuilder, private userService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  login() {
    if (this.loginForm.valid) {
      const body = this.loginForm.value;

      this.userService.userLogin(body).subscribe({
        next: (response) => {
          

          localStorage.setItem('user', JSON.stringify(response));
          localStorage.setItem('accessToken', response.token);
          localStorage.setItem('refreshToken', response.refreshToken);

          
          this.router.navigate(['/home']);
        },
        error: (error) => {
          if (error.status === 401) {
            alert('Credenciales incorrectas. Por favor, verifica tu usuario y contraseña.');
            this.router.navigate(['/home']);
          } else if (
            error.error &&
            typeof error.error === 'string' &&
            error.error.toLowerCase().includes('full authentication')
          ) {
            alert('Debes iniciar sesión para acceder.');
            this.router.navigate(['/home']);
          } else {
            console.error('Error inesperado:', error);
          }
        }
      });
    } else {
      alert('Por favor, completa todos los campos.');
    }
  }
}

