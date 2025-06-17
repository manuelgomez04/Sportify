import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComentariosUsuarioPage } from '../models/comentario/comentario.model';

@Injectable({ providedIn: 'root' })
export class ComentariosService {
  constructor(private http: HttpClient) {}

  getMisComentarios(page: number, size: number): Observable<ComentariosUsuarioPage> {
    return this.http.get<ComentariosUsuarioPage>(`/comentario/me?page=${page}&size=${size}`);
  }

  editarComentario(slug: string, body: { titulo: string; comentario: string }): Observable<any> {
    return this.http.put(`/comentario/${slug}`, body);
  }

  eliminarComentario(slug: string): Observable<any> {
    return this.http.delete(`/comentario/${slug}`);
  }
}
