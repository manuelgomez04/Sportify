import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { NoticiasService, NoticiasPage } from '../../services/noticias.service';
import { Noticia } from '../../models/noticia/noticia.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  noticias: Noticia[] = [];
  noticiasPage?: NoticiasPage;
  page = 0;
  size = 6;
  nombre: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private noticiasService: NoticiasService
  ) { }

  ngOnInit() {
    const username = this.getUsernameFromToken();
    if (username) {
      this.authService.getUsuario(username).subscribe(usuario => {
        this.nombre = usuario.nombre;
      });
    }
    this.cargarNoticias();
  }

  cargarNoticias(page: number = 0) {
    this.noticiasService.getNoticias(page, this.size).subscribe({
      next: resp => {
        this.noticiasPage = resp;
        this.noticias = resp.content || [];
        this.page = resp.number;
      },
      error: err => {
        this.noticias = [];
        // Puedes mostrar un mensaje de error aquí si quieres
      }
    });
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
  getMultimediaUrl(url: string): string {
    if (!url) return '';

    const cleanUrl = url.trim();

    if (cleanUrl.startsWith('http') || cleanUrl.startsWith('/download')) {
      return cleanUrl;
    }

  

    return '/download/' + cleanUrl;
  }


}
