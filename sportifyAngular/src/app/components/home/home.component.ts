import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { NoticiasService, NoticiasPage } from '../../services/noticias.service';
import { Noticia } from '../../models/noticia/noticia.model';
import { HttpClient } from '@angular/common/http';

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
  isLoggedIn = false;
  likedTitulares: Set<string> = new Set();

  constructor(
    private authService: AuthService,
    private router: Router,
    private noticiasService: NoticiasService,
    private http: HttpClient // Necesario para la petición de likes
  ) { }

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
    this.cargarNoticiasYLikes();
  }

  cargarNoticiasYLikes(page: number = 0) {
    if (this.isLoggedIn) {
      this.http.get<any>('/noticiasLiked').subscribe({
        next: resp => {
          const likedNoticias = resp.noticiasLiked?.content || [];
          this.likedTitulares = new Set(likedNoticias.map((n: any) => n.titular));
          this.cargarNoticias(page);
        },
        error: () => {
          this.likedTitulares = new Set();
          this.cargarNoticias(page);
        }
      });
    } else {
      this.likedTitulares = new Set();
      this.cargarNoticias(page);
    }
  }

  cargarNoticias(page: number = 0) {
    this.noticiasService.getNoticias(page, this.size).subscribe({
      next: resp => {
        this.noticiasPage = resp;
        // Crea una copia de cada noticia y añade el campo liked SOLO en el array local
        this.noticias = (resp.content || []).map(noticia => {
          const noticiaConLike = Object.assign({}, noticia);
          // Añade el campo liked solo en el array local, nunca en el modelo global
          (noticiaConLike as any).liked = this.likedTitulares.has(noticia.titular);
          return noticiaConLike;
        });
        this.page = resp.number;
      },
      error: err => {
        this.noticias = [];
      }
    });
  }

  getMultimediaUrl(url: string): string {
    if (!url) return '';

    const cleanUrl = url.trim();

    if (cleanUrl.startsWith('http') || cleanUrl.startsWith('/download')) {
      return cleanUrl;
    }

    return '/download/' + cleanUrl;
  }

  cambiarPagina(page: number) {
    if (page < 0 || (this.noticiasPage && page >= this.noticiasPage.totalPages) || page === this.page) {
      return;
    }
    this.page = page;
    this.cargarNoticias(page);
  }

  verDetalle(slug: string) {
    this.router.navigate(['/noticias', slug]);
  }

  toggleLike(noticia: any) {
    this.noticiasService.likeNoticia(noticia.slug).subscribe({
      next: () => {
        noticia.liked = !noticia.liked;
        noticia.likesCount = noticia.likesCount ? noticia.likesCount + (noticia.liked ? 1 : -1) : 1;
        if (noticia.liked) {
          this.likedTitulares.add(noticia.slug);
        } else {
          this.likedTitulares.delete(noticia.slug);
        }
      },
      error: () => {
        // Opcional: mostrar error
      }
    });
  }
}

