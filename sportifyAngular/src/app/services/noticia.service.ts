import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class NoticiaService {
  constructor(private http: HttpClient) {}

  addDeporte(slug: string, nombreDeporte: string) {
    return this.http.put(`/noticias/addDeporte/${slug}`, { nombreDeporte });
  }

  addEquipo(slug: string, nombreEquipo: string) {
    return this.http.put(`/noticias/addEquipo/${slug}`, { nombreEquipo });
  }

  addLiga(slug: string, nombreLiga: string) {
    return this.http.put(`/noticias/addLiga/${slug}`, { nombreLiga });
  }
}
