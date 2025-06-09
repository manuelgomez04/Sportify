import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  nombre: string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    const username = this.getUsernameFromToken(); // Implementa esta función según tu token
    if (username) {
      this.authService.getUsuario(username).subscribe(usuario => {
        this.nombre = usuario.nombre;
      });
    }
  }
  get isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }
  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  // Ejemplo simple si el username está en el token (JWT)
  getUsernameFromToken(): string | null {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || payload.username || null;
    } catch {
      return null;
    }
  }
}
