import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DeportePage } from '../models/deporte/deporte.model';

@Injectable({ providedIn: 'root' })
export class DeportesService {
  constructor(private http: HttpClient) {}

  getDeportes(page: number, size: number): Observable<DeportePage> {
    return this.http.get<DeportePage>(`/deporte?page=${page}&size=${size}`);
  }

  getDeportesSeguidos(): Observable<any> {
    return this.http.get<any>('/me');
  }

  seguirDeporte(nombreNoEspacio: string): Observable<any> {
    return this.http.put('/seguirDeporte', { nombreDeporte: nombreNoEspacio });
  }

  unfollowDeporte(nombreNoEspacio: string): Observable<any> {
    return this.http.put('/unfollowDeporte', { nombreDeporte: nombreNoEspacio });
  }

  getDeportesFavoritos(page: number, size: number) {
    return this.http.get<any>(`/deportesFavoritos?page=${page}&size=${size}`);
  }

  eliminarDeporte(nombreNoEspacio: string) {
    return this.http.delete(`/deporte/${nombreNoEspacio}`);
  }

  crearDeporte(formData: FormData) {
    return this.http.post('/deporte', formData);
  }
}
