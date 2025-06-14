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
          this.likedTitulares = new Set(likedNoticias.map((n: any) => n.slug));
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
    console.log('Llamando a /noticiasLiked...');

    this.noticiasService.getNoticias(page, this.size).subscribe({
      next: resp => {
        console.log('Respuesta de /noticiasLiked:', resp);
        this.noticiasPage = resp;
        this.noticias = (resp.content || []).map(noticia => {
          const noticiaConLike = Object.assign({}, noticia);
          // Usa slug para comprobar el like
          (noticiaConLike as any).liked = this.likedTitulares.has(noticia.slug);
          return noticiaConLike;
        });
        this.page = resp.number;
      },
      error: err => {
        console.error('Error al cargar noticias:', err);
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

  toggleLike(noticia: any, event: Event) {
    event.stopPropagation();
    const wasLiked = this.likedTitulares.has(noticia.slug);

    if (wasLiked) {
      this.noticiasService.unlikeNoticia(noticia.slug).subscribe({
        next: () => {
          this.likedTitulares.delete(noticia.slug);
          noticia.likesCount = noticia.likesCount ? noticia.likesCount - 1 : 0;
        },
        error: () => {
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      this.noticiasService.likeNoticia(noticia.slug).subscribe({
        next: () => {
          this.likedTitulares.add(noticia.slug);
          noticia.likesCount = noticia.likesCount ? noticia.likesCount + 1 : 1;
        },
        error: () => {
          (event.target as HTMLInputElement).checked = false;
        }
      });
    }
  }
}