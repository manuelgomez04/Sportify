import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Noticia } from '../../models/noticia/noticia.model';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NoticiasService } from '../../services/noticias.service';

@Component({
  selector: 'app-noticias-favoritas',
  templateUrl: './noticias-favoritas.component.html',
  styleUrls: ['./noticias-favoritas.component.css']
})
export class NoticiasFavoritasComponent implements OnInit {
  noticias: Noticia[] = [];
  page = 0;
  size = 4;
  totalPages = 0;
  isLoggedIn = false;
  likedTitulares: Set<string> = new Set();

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    private noticiasService: NoticiasService
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
    this.cargarLikesFavoritas();
    this.cargarNoticias(0);
  }

  cargarLikesFavoritas() {
    if (this.isLoggedIn) {
      this.http.get<any>('/noticiasLiked').subscribe({
        next: resp => {
          const likedNoticias = resp.noticiasLiked?.content || [];
          this.likedTitulares = new Set(likedNoticias.map((n: any) => n.slug));
        },
        error: () => {
          this.likedTitulares = new Set();
        }
      });
    } else {
      this.likedTitulares = new Set();
    }
  }

  cargarNoticias(page: number) {
    this.http.get<any>(`/noticiasLiked?page=${page}&size=${this.size}`).subscribe({
      next: resp => {
        this.noticias = resp.noticiasLiked?.content || [];
        this.page = resp.noticiasLiked?.number || 0;
        this.totalPages = resp.noticiasLiked?.totalPages || 0;
      }
    });
  }

  toggleLike(noticia: any, event: Event) {
    event.stopPropagation();
    // El checkbox cambia su estado ANTES de que se dispare (change)
    // Por eso, para saber el estado ANTES del cambio, hay que comprobar el set antes de modificarlo
    const wasLiked = this.likedTitulares.has(noticia.slug);
    console.log('wasLiked:', wasLiked, 'slug:', noticia.slug);
    // Si estaba likeado antes del click, ahora hay que hacer DELETE
    if (wasLiked) {
      this.noticiasService.unlikeNoticia(noticia.slug).subscribe({
        next: () => {
          this.likedTitulares.delete(noticia.slug);
          noticia.likesCount = noticia.likesCount ? noticia.likesCount - 1 : 0;
        },
        error: () => {
          // Si hay error, vuelve a marcar el checkbox visualmente
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      // Si no estaba likeado antes del click, ahora hay que hacer POST
      this.noticiasService.likeNoticia(noticia.slug).subscribe({
        next: () => {
          this.likedTitulares.add(noticia.slug);
          noticia.likesCount = noticia.likesCount ? noticia.likesCount + 1 : 1;
        },
        error: () => {
          // Si hay error, desmarca el checkbox visualmente
          (event.target as HTMLInputElement).checked = false;
        }
      });
    }
  }

  cambiarPagina(p: number) {
    if (p >= 0 && p < this.totalPages) {
      this.cargarNoticias(p);
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

  verDetalle(slug: string) {
    this.router.navigate(['/noticias', slug]);
  }
}

