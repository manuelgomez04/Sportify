import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ComentarioDetalle, ComentariosDetallePage, NoticiaDetalle } from '../../models/noticia/noticia-detalle.model';
import { AuthService } from '../../services/auth.service';

export interface UsuarioDetalle {
  username: string;
  fotoPerfil: string | null;
}



@Component({
  selector: 'app-detalle-noticia',
  templateUrl: './detalle-noticia.component.html',
  styleUrls: ['./detalle-noticia.component.css']
})
export class DetalleNoticiaComponent implements OnInit {
  slug: string = '';
  noticia: NoticiaDetalle | null = null;
  comentarios: ComentarioDetalle[] = [];
  comentariosPage: ComentariosDetallePage | null = null;
  page = 0;
  size = 10;
  comentarioForm: FormGroup;
  comentarioError: string | null = null;
  comentarioSuccess: string | null = null;
  isLoggedIn = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.comentarioForm = this.fb.group({
      tituloComentario: ['', [Validators.required, Validators.maxLength(100)]],
      comentario: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.slug = params.get('slug') || '';
      this.cargarNoticiaYComentarios(0);
    });
    this.isLoggedIn = this.authService.isAuthenticated();
  }

  cargarNoticiaYComentarios(page: number) {
    this.http.get<NoticiaDetalle>(`/noticias/${this.slug}/comentarios?page=${page}&size=${this.size}`).subscribe(resp => {
      this.noticia = resp;
      this.comentarios = resp.comentarios?.content || [];
      this.comentariosPage = resp.comentarios;
      this.page = this.comentariosPage?.number || 0;
    });
  }

  cambiarPaginaComentarios(p: number) {
    if (this.comentariosPage && p >= 0 && p < this.comentariosPage.totalPages) {
      this.cargarNoticiaYComentarios(p);
    }
  }

  enviarComentario() {
    if (!this.isLoggedIn) {
      this.comentarioError = 'Debes estar autenticado para poder escribir un comentario';
      setTimeout(() => this.comentarioError = null, 4000);
      return;
    }
    if (!this.noticia || this.comentarioForm.invalid) return;
    const body = {
      titular: this.slug,
      titulo: this.comentarioForm.value.tituloComentario,
      comentario: this.comentarioForm.value.comentario
    };
    this.http.post('/comentario', body).subscribe({
      next: () => {
        this.comentarioSuccess = 'Comentario enviado correctamente';
        this.comentarioForm.reset();
        this.cargarNoticiaYComentarios(this.page);
        setTimeout(() => this.comentarioSuccess = null, 2000);
      },
      error: (err) => {
        // Manejo de error de validación del backend
        if (
          err.error &&
          err.error['invalid-params'] &&
          Array.isArray(err.error['invalid-params']) &&
          err.error['invalid-params'].length > 0
        ) {
          const param = err.error['invalid-params'][0];
          this.comentarioError = param.message || 'Error de validación';
        } else if (err.error && err.error.detail) {
          this.comentarioError = err.error.detail;
        } else {
          this.comentarioError = 'Error al enviar el comentario';
        }
        setTimeout(() => this.comentarioError = null, 3000);
      }
    });
  }
}
