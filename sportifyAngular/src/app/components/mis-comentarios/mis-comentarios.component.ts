import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarComentarios(0);
  }

  cargarComentarios(page: number) {
    this.http.get<ComentariosUsuarioPage>(`/comentario/me?page=${page}&size=${this.size}`).subscribe({
      next: resp => {
        this.comentarios = resp.content || [];
        this.page = resp.number || 0;
        this.totalPages = resp.totalPages || 0;
      }
    });
  }

  cambiarPagina(p: number) {
    if (p >= 0 && p < this.totalPages) {
      this.cargarComentarios(p);
    }
  }
}
