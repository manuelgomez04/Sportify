import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ComentarioDetalle, ComentariosDetallePage, NoticiaDetalle } from '../../models/noticia/noticia-detalle.model';
import { AuthService } from '../../services/auth.service';
import { NoticiasService } from '../../services/noticias.service';

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
  totalPages = 0;
  comentarioForm: FormGroup;
  comentarioError: string | null = null;
  comentarioSuccess: string | null = null;
  isLoggedIn = false;
  likedTitulares: Set<string> = new Set();

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private authService: AuthService,
    private noticiasService: NoticiasService
  ) {
    this.comentarioForm = this.fb.group({
      tituloComentario: ['', [Validators.required, Validators.maxLength(100)]],
      comentario: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
    this.cargarLikesDetalle();
    this.route.paramMap.subscribe(params => {
      this.slug = params.get('slug') || '';
      this.comentarios = [];
      this.cargarNoticiaYComentarios(0);
    });
  }

  cargarLikesDetalle() {
    if (this.isLoggedIn) {
      this.noticiasService.getNoticiasLiked().subscribe({
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

  cargarNoticiaYComentarios(page: number) {
    this.noticiasService.getNoticiaDetalleConComentarios(this.slug, page, this.size).subscribe(resp => {
      this.noticia = resp;
      if (page === 0) {
        this.comentarios = resp.comentarios?.content || [];
      } else {
        this.comentarios = this.comentarios.concat(resp.comentarios?.content || []);
      }
      this.comentariosPage = resp.comentarios;
      this.page = this.comentariosPage?.number || 0;
      this.totalPages = this.comentariosPage?.totalPages || 0;
    });
  }

  cargarMasComentarios() {
    if (this.page + 1 < this.totalPages) {
      this.cargarNoticiaYComentarios(this.page + 1);
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
    this.noticiasService.enviarComentario(body).subscribe({
      next: () => {
        this.comentarioSuccess = 'Comentario enviado correctamente';
        this.comentarioForm.reset();
        this.comentarios = [];
        this.cargarNoticiaYComentarios(0);
        setTimeout(() => this.comentarioSuccess = null, 2000);
      },
      error: (err) => {
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

  getFotoPerfilUrl(fotoPerfil: string | null): string {
    if (!fotoPerfil) return 'assets/default-profile.png';
    if (fotoPerfil.startsWith('http')) return fotoPerfil;
    return '/download/' + fotoPerfil;
  }
  toggleLikeDetalle(noticia: any, event: Event) {
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
