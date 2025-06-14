import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ComentarioUsuario, ComentariosUsuarioPage } from '../../models/comentario/comentario.model';

@Component({
  selector: 'app-mis-comentarios',
  templateUrl: './mis-comentarios.component.html',
  styleUrls: ['./mis-comentarios.component.css']
})
export class MisComentariosComponent implements OnInit {
  comentarios: ComentarioUsuario[] = [];
  page = 0;
  size = 5;
  totalPages = 0;
  editandoComentario: ComentarioUsuario | null = null;
  editComentarioForm: FormGroup;
  editLoading = false;
  editError: string | null = null;
  editSuccess: boolean = false;
  eliminandoComentario: ComentarioUsuario | null = null;
  deleteLoading = false;
  deleteError: string | null = null;

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.editComentarioForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(100)]],
      comentario: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit() {
    this.comentarios = [];
    this.cargarComentarios(0);
  }

  cargarComentarios(page: number) {
    this.http.get<ComentariosUsuarioPage>(`/comentario/me?page=${page}&size=${this.size}`).subscribe({
      next: resp => {
        // Si es la primera página, reemplaza. Si no, acumula.
        if (page === 0) {
          this.comentarios = resp.content || [];
        } else {
          this.comentarios = this.comentarios.concat(resp.content || []);
        }
        this.page = resp.number || 0;
        this.totalPages = resp.totalPages || 0;
      }
    });
  }

  cargarMasComentarios() {
    if (this.page + 1 < this.totalPages) {
      this.cargarComentarios(this.page + 1);
    }
  }

  abrirEditar(comentario: ComentarioUsuario) {
    this.editandoComentario = comentario;
    this.editComentarioForm.patchValue({
      titulo: comentario.titular,
      comentario: comentario.comentario
    });
    this.editError = null;
    this.editSuccess = false;
  }

  cerrarEditar() {
    this.editandoComentario = null;
    this.editComentarioForm.reset();
    this.editError = null;
    this.editSuccess = false;
  }

  guardarEdicion() {
    if (!this.editandoComentario || this.editComentarioForm.invalid) return;
    this.editLoading = true;
    this.editError = null;
    this.editSuccess = false;
    // Usa el campo correcto para el slug de la noticia asociada
    const slug = this.editandoComentario.noticia.slug || this.editandoComentario.noticia.slug || this.editandoComentario.titular;
    const body = {
      titulo: this.editComentarioForm.value.titulo,
      comentario: this.editComentarioForm.value.comentario
    };
    this.http.put(`/comentario/${slug}`, body).subscribe({
      next: () => {
        this.editSuccess = true;
        this.cargarComentarios(this.page);
        setTimeout(() => {
          this.cerrarEditar();
        }, 1200);
      },
      error: (err) => {
        if (
          err.error &&
          err.error['invalid-params'] &&
          Array.isArray(err.error['invalid-params']) &&
          err.error['invalid-params'].length > 0
        ) {
          this.editError = err.error['invalid-params']
            .map((param: any) =>
              `<strong>${param.field}:</strong> ${param.message} <span class="text-muted">(Valor: "${param.rejectedValue}")</span>`
            ).join('<br>');
        } else if (err.error && err.error.detail) {
          this.editError = `<strong>Error:</strong> ${err.error.detail}`;
        } else {
          this.editError = 'Error al editar el comentario';
        }
      },
      complete: () => {
        this.editLoading = false;
      }
    });
  }

  abrirEliminar(comentario: ComentarioUsuario) {
    this.eliminandoComentario = comentario;
    this.deleteError = null;
  }

  cerrarEliminar() {
    this.eliminandoComentario = null;
    this.deleteError = null;
  }

  eliminarComentarioConfirmado() {
    if (!this.eliminandoComentario) return;
    this.deleteLoading = true;
    this.deleteError = null;
    const slug = this.eliminandoComentario.noticia.slug || this.eliminandoComentario.noticia.slug || this.eliminandoComentario.titular;
    this.http.delete(`/comentario/${slug}`).subscribe({
      next: () => {
        this.cerrarEliminar();
        this.comentarios = [];
        this.page = 0;
        this.cargarComentarios(0);
      },
      error: (err) => {
        this.deleteError = 'Error al eliminar el comentario';
      },
      complete: () => {
        this.deleteLoading = false;
      }
    });
  }
}
