import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css']
})
export class MeComponent implements OnInit {
  userData: any = null;
  error: boolean = false;

  constructor(
    protected authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    // El guard ya protege la ruta, aquí puedes cargar datos si quieres
  }

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

