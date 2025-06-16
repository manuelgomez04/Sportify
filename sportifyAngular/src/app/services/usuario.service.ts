import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  constructor(private http: HttpClient) {}

  getUsuario(username: string): Observable<any> {
    return this.http.get('/me');
  }

  editarUsuario(formData: FormData): Observable<any> {
    return this.http.put('/edit/me', formData);
  }
}
