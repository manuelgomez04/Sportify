import { Component, OnInit } from '@angular/core';
import { Noticia } from '../../models/noticia/noticia.model';
import { Router } from '@angular/router';
import { MisNoticiasService } from '../../services/mis-noticias.service';

@Component({
  selector: 'app-mis-noticias',
  templateUrl: './mis-noticias.component.html',
  styleUrls: ['./mis-noticias.component.css']
})
export class MisNoticiasComponent implements OnInit {
  noticias: Noticia[] = [];
  page = 0;
  size = 6;
  totalPages = 0;


  editandoNoticia: Noticia | null = null;
  editTitular = '';
  editCuerpo = '';
  editArchivos: File[] = [];
  editLoading = false;
  editError: string | null = null;
  editSuccess = false;


  noticiaAEliminar: Noticia | null = null;
  deleteLoading = false;
  deleteError: string | null = null;

  constructor(
    private misNoticiasService: MisNoticiasService,
    private router: Router
  ) { }

  ngOnInit() {
    this.cargarNoticias(0);
  }

  cargarNoticias(page: number) {
    this.misNoticiasService.getMisNoticias(page, this.size).subscribe({
      next: resp => {
        this.noticias = resp.content || [];
        this.page = resp.number || 0;
        this.totalPages = resp.totalPages || 0;
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

    if (cleanUrl.startsWith('http://app:8080')) {
      return cleanUrl.replace('http://app:8080', window.location.origin);
    }

    if (cleanUrl.startsWith('http') || cleanUrl.startsWith('/download')) {
      return cleanUrl;
    }

    return '/download/' + cleanUrl;
  }
  abrirEditar(noticia: Noticia) {
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

  abrirEliminar(noticia: Noticia) {
    this.noticiaAEliminar = noticia;
    this.deleteError = null;
  }

  cerrarEliminar() {
    this.noticiaAEliminar = null;
    this.deleteError = null;
  }

  eliminarNoticia() {
    if (!this.noticiaAEliminar) return;
    this.deleteLoading = true;
    this.deleteError = null;
    this.misNoticiasService.eliminarNoticia(this.noticiaAEliminar.slug).subscribe({
      next: () => {
        this.cerrarEliminar();
        this.cargarNoticias(this.page);
      },
      error: () => {
        this.deleteError = 'Error al eliminar la noticia';
      },
      complete: () => {
        this.deleteLoading = false;
      }
    });
  }

  verDetalle(slug: string) {
    this.router.navigate(['/noticias', slug]);
  }
}
