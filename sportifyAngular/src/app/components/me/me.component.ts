import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css']
})
export class MeComponent {
  constructor(
    private authService: AuthService,
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
}
