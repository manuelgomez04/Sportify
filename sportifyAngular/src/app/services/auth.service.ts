import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { userLogin } from '../../model/user/model-login';
import { miUsuario } from '../../model/user/miUsuario';

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient) { }

    userLogin(body: { username: string; password: string }): Observable<userLogin> {
        return this.http.post<userLogin>('/auth/login', body);
    }
    getUsuario(username: string) {
        return this.http.get<miUsuario>(`/me`);
    }
    logout() {
        return this.http.post('/auth/logout', {}).subscribe({
            next: () => {
                localStorage.clear();
                // Aquí puedes hacer más limpieza si lo necesitas
            },
            error: () => {
                localStorage.clear();
            }
        });
    }
}