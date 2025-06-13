import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Noticia } from '../../models/noticia/noticia.model';
import { Router } from '@angular/router';

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

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarNoticias(0);
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

