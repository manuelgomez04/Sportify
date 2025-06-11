import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Noticia } from '../models/noticia.model';

export interface NoticiasPage {
  content: Noticia[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  // ...otros campos de paginación si los necesitas
}

@Injectable({ providedIn: 'root' })
export class NoticiasService {
  constructor(private http: HttpClient) {}

  getNoticias(page: number = 0, size: number = 6): Observable<NoticiasPage> {
    const token = localStorage.getItem('accessToken');
    let options = {};
    
    return this.http.get<NoticiasPage>(
      `/noticias?page=${page}&size=${size}&sort=fechaPublicacion,DESC`,
      options
    );
  }
}
