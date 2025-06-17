import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Noticia } from '../models/noticia/noticia.model';

@Injectable({ providedIn: 'root' })
export class MisNoticiasService {
  constructor(private http: HttpClient) {}

  getMisNoticias(page: number, size: number): Observable<{ content: Noticia[], number: number, totalPages: number }> {
    return this.http.get<{ content: Noticia[], number: number, totalPages: number }>(`/noticias/me?page=${page}&size=${size}`);
  }

  editarNoticia(slug: string, formData: FormData): Observable<any> {
    return this.http.put(`/noticias/edit/${slug}`, formData);
  }

  eliminarNoticia(slug: string): Observable<any> {
    return this.http.delete(`/noticias/delete/${slug}`);
  }
}
