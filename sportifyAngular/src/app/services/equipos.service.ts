import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Equipo, EquipoPage } from '../models/equipo/equipo.model';

@Injectable({ providedIn: 'root' })
export class EquiposService {
  constructor(private http: HttpClient) { }

  getEquiposPorLiga(nombreLigaNoEspacio: string, page: number, size: number): Observable<EquipoPage> {
    return this.http.get<EquipoPage>(`/equipo/${nombreLigaNoEspacio}?page=${page}&size=${size}`);
  }

  getEquiposSeguidos(): Observable<any> {
    return this.http.get<any>('/me');
  }

  seguirEquipo(nombreNoEspacio: string): Observable<any> {
    return this.http.put('/seguirEquipo', { nombreEquipo: nombreNoEspacio });
  }

  unfollowEquipo(nombreNoEspacio: string): Observable<any> {
    return this.http.put('/unfollowEquipo', { nombreEquipo: nombreNoEspacio });
  }

  getEquiposFavoritos(page: number, size: number) {
    return this.http.get<any>(`/equiposFavoritos?page=${page}&size=${size}`);
  }

  getEquiposPorLigaSinPaginacion(nombreLigaNoEspacio: string): Observable<any[]> {
    return this.http.get<any[]>(`/equipo/por-liga/${nombreLigaNoEspacio}`);
  }

  getAllEquipos(page: number, size: number) {
    return this.http.get<Equipo[]>('/equipo');
  }

  getAllEquiposSinPaginacion() {
    return this.http.get<Equipo[]>('/equipo');
  }
  eliminarEquipo(nombreNoEspacio: string) {
    return this.http.delete(`/equipo/${nombreNoEspacio}`);
  }

  crearEquipo(formData: FormData) {
    return this.http.post('/equipo', formData);
  }
}
