import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Noticia } from '../models/noticia/noticia.model';
import { NoticiaDetalle } from '../models/noticia/noticia-detalle.model';

export interface NoticiasPage {
  content: Noticia[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class NoticiasService {
  constructor(private http: HttpClient) { }

  getNoticias(page: number = 0, size: number = 6): Observable<NoticiasPage> {
    const token = localStorage.getItem('accessToken');
    let options = {};
    return this.http.get<NoticiasPage>(
      `/noticias?page=${page}&size=${size}&sort=fechaPublicacion,DESC`,
      options
    );
  }
  likeNoticia(slug: string): Observable<any> {
    return this.http.post('/like', { titular: slug });
  }

  unlikeNoticia(slug: string): Observable<any> {
    return this.http.delete(`/like/${slug}`);
  }

  getNoticiasLiked(): Observable<any> {
    return this.http.get<any>('/noticiasLiked');
  }

  getNoticiasLikedPage(page: number, size: number) {
    return this.http.get<any>(`/noticiasLiked?page=${page}&size=${size}`);
  }

  getNoticiaDetalleConComentarios(slug: string, page: number, size: number): Observable<NoticiaDetalle> {
    return this.http.get<NoticiaDetalle>(`/noticias/${slug}/comentarios?page=${page}&size=${size}`);
  }

  enviarComentario(body: { titular: string; titulo: string; comentario: string }): Observable<any> {
    return this.http.post('/comentario', body);
  }
}
