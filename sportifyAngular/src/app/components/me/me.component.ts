import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css']
})
export class MeComponent {
  userData: any = null;
  error: boolean = false;

  showPasswordModal = false;
  passwordForm = {
    oldPassword: '',
    password: '',
    verifyPassword: ''
  };
  passwordError: string = '';
  passwordSuccess: string = '';

  showOldPassword = false;
  showNewPassword = false;
  showVerifyPassword = false;

  constructor(
    protected authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}


  logout() {
    this.authService.logout();
    this.router.navigate(['/home']);
  }

  eliminarCuenta() {
    if (confirm('¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.')) {
      this.http.delete('/delete/me').subscribe({
        next: () => {
          this.authService.logout();
          this.router.navigate(['/home']);
        },
        error: () => {
          alert('Error al eliminar la cuenta');
        }
      });
    }
  }

  openPasswordModal() {
    this.passwordForm = { oldPassword: '', password: '', verifyPassword: '' };
    this.passwordError = '';
    this.passwordSuccess = '';
    this.showPasswordModal = true;
  }

  closePasswordModal() {
    this.showPasswordModal = false;
  }

  submitPasswordChange() {
    this.passwordError = '';
    this.passwordSuccess = '';
    const { oldPassword, password, verifyPassword } = this.passwordForm;
    if (!oldPassword || !password || !verifyPassword) {
      this.passwordError = 'Todos los campos son obligatorios.';
      return;
    }
    if (password !== verifyPassword) {
      this.passwordError = 'Las contraseñas no coinciden.';
      return;
    }
    this.http.put('/edit/password', {
      oldPassword,
      password,
      verifyPassword
    }).subscribe({
      next: () => {
        this.passwordSuccess = 'Contraseña cambiada correctamente';
        setTimeout(() => this.closePasswordModal(), 1200);
      },
      error: (err) => {
        let msg = 'Error al cambiar la contraseña';
        if (err?.error?.message) msg = err.error.message;
        this.passwordError = msg;
      }
    });
  }
}

