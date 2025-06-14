import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LigaPage } from '../models/liga/liga.model';

@Injectable({ providedIn: 'root' })
export class LigasService {
  constructor(private http: HttpClient) {}

  getLigas(page: number, size: number): Observable<LigaPage> {
    return this.http.get<LigaPage>(`/liga?page=${page}&size=${size}`);
  }

  getLigasPorDeporte(nombreDeporte: string, page: number, size: number): Observable<LigaPage> {
    return this.http.get<LigaPage>(`/liga/${nombreDeporte}?page=${page}&size=${size}`);
  }

  getLigasSeguidas(): Observable<any> {
    return this.http.get<any>('/me');
  }

  seguirLiga(nombreSinEspacio: string) {
    return this.http.put('/seguirLiga', { nombreLiga: nombreSinEspacio });
  }

  unfollowLiga(nombreSinEspacio: string) {
    return this.http.put('/unfollowLiga', { nombreLiga: nombreSinEspacio });
  }

  getLigasFavoritas(page: number, size: number) {
    return this.http.get<any>(`/ligasFavoritas?page=${page}&size=${size}`);
  }
}
