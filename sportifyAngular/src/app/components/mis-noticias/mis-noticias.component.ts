import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Noticia } from '../../models/noticia/noticia.model';

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

  // Edición
  editandoNoticia: Noticia | null = null;
  editTitular = '';
  editCuerpo = '';
  editArchivos: File[] = [];
  editLoading = false;
  editError: string | null = null;
  editSuccess = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarNoticias(0);
  }

  cargarNoticias(page: number) {
    this.http.get<{ content: Noticia[], number: number, totalPages: number }>(`/noticias/me?page=${page}&size=${this.size}`).subscribe({
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

    this.http.put(`/noticias/edit/${this.editandoNoticia.slug}`, formData).subscribe({
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
