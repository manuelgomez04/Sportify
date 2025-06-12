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
¡
}
