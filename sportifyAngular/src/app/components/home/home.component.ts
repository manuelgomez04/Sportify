import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { NoticiasService, NoticiasPage } from '../../services/noticias.service';
import { MisNoticiasComponent } from '../mis-noticias/mis-noticias.component';
import { Noticia } from '../../models/noticia/noticia.model';
import { HttpClient } from '@angular/common/http';
import { MisNoticiasService } from '../../services/mis-noticias.service';

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
  isAdmin = false;
  likedTitulares: Set<string> = new Set();
  totalNoticias = 0;

  filtros: any = {
    slug: '',
    fechaInicio: '',
    fechaFin: '',
    deporte: '',
    liga: '',
    username: ''
  };

  editandoNoticia: Noticia | null = null;
  editTitular = '';
  editCuerpo = '';
  editArchivos: File[] = [];
  editLoading = false;
  editError: string | null = null;
  editSuccess = false;
  showEditModal = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private noticiasService: NoticiasService,
    private http: HttpClient,
    private misNoticiasService: MisNoticiasService
  ) { }

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
    this.isAdmin = this.authService.isAdmin();
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

    const params: any = {
      ...this.filtros,
      page: this.page,
      size: this.size
    };
    // Elimina filtros vacíos
    Object.keys(params).forEach(k => (params[k] === '' || params[k] == null) && delete params[k]);

    this.noticiasService.getNoticiasFiltradas(params).subscribe({
      next: resp => {
        console.log('Respuesta de /noticiasLiked:', resp);
        this.noticiasPage = resp;
        this.noticias = (resp.content || []).map((noticia: { slug: string; }) => {
          const noticiaConLike = Object.assign({}, noticia);
          // Usa slug para comprobar el like
          (noticiaConLike as any).liked = this.likedTitulares.has(noticia.slug);
          return noticiaConLike;
        });
        this.page = resp.number;
        this.totalNoticias = resp.totalElements || 0;
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

  setPage(p: number) {
    if (p < 0) return;
    this.page = p;
    this.cargarNoticias();
  }

  nextPage() {
    if ((this.page + 1) * this.size < this.totalNoticias) {
      this.page++;
      this.cargarNoticias();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.cargarNoticias();
    }
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

  // Llama a esto cuando cambie algún filtro
  onFiltroChange() {
    this.page = 0;
    this.cargarNoticias();
  }

  getPagesArray() {
    return Array(Math.ceil(this.totalNoticias / this.size)).fill(0);
  }

  editarNoticia(noticia: Noticia, event: Event) {
    event.stopPropagation();
    this.router.navigate(['/noticias', noticia.slug, 'editar']);
  }

  borrarNoticia(noticia: Noticia, event: Event) {
    event.stopPropagation();
    if (confirm('¿Seguro que quieres borrar esta noticia?')) {
      this.misNoticiasService.eliminarNoticia(noticia.slug).subscribe({
        next: () => this.cargarNoticias(),
        error: () => alert('Error al borrar la noticia')
      });
    }
  }

  abrirEditar(noticia: Noticia, event: Event) {
    event.stopPropagation();
    this.editandoNoticia = noticia;
    this.editTitular = noticia.titular;
    this.editCuerpo = noticia.cuerpo;
    this.editArchivos = [];
    this.editError = null;
    this.editSuccess = false;
  }

  cerrarEditar() {
    this.editandoNoticia = null;
    this.editTitular = '';
    this.editCuerpo = '';
    this.editArchivos = [];
    this.editError = null;
    this.editSuccess = false;
  }

  onEditFilesSelected(event: any) {
    this.editArchivos = Array.from(event.target.files);
  }

  guardarEdicion() {
    if (!this.editandoNoticia) return;
    this.editLoading = true;
    this.editError = null;
    this.editSuccess = false;

    const formData = new FormData();
    const editDto = {
      titular: this.editTitular,
      cuerpo: this.editCuerpo
    };
    formData.append('editNoticiaDto', new Blob([JSON.stringify(editDto)], { type: 'application/json' }));
    this.editArchivos.forEach(file => formData.append('files', file));

    this.misNoticiasService.editarNoticia(this.editandoNoticia.slug, formData).subscribe({
      next: () => {
        this.editSuccess = true;
        this.cargarNoticias(this.page);
        setTimeout(() => this.cerrarEditar(), 1200);
      },
      error: () => {
        this.editError = 'Error al editar la noticia';
      },
      complete: () => {
        this.editLoading = false;
      }
    });
  }
}