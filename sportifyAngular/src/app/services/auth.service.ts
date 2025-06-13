import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { userLogin } from '../models/user/model-login';
import { miUsuario } from '../models/user/miUsuario';
import { UserRegister } from '../models/user/model-register';
import { ActivateAccount } from '../models/user/model-activate-account';
import { jwtDecode } from 'jwt-decode';

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

            },
            error: () => {
                localStorage.clear();
            }
        });
    }

    register(data: any, userType: string) {
        let url = userType === 'writer' ? '/writer/auth/register' : '/user/auth/register';
        // Si es FormData, no pongas Content-Type
        if (data instanceof FormData) {
            return this.http.post(url, data); // NO pongas headers aquí
        } else {
            return this.http.post(url, data, { headers: { 'Content-Type': 'application/json' } });
        }
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('user');
    }

    getAuthorizationHeader(): string {
        return 'Bearer ' + localStorage.getItem('accessToken');
    }

    verifyAccount(body: { token: string }) {
        return this.http.post('/activate/account/', body);

    }

    // Añade este método para obtener los roles del usuario autenticado desde el token o el usuario guardado
    getRoles(): string[] {
        const userStr = localStorage.getItem('user') || sessionStorage.getItem('user');
        if (userStr) {
            try {
                const user = JSON.parse(userStr);
                if (user.token) {
                    const decoded: any = jwtDecode(user.token);
                    // DEBUG: muestra el payload y los roles
                    console.log('JWT payload:', decoded);
                    if (decoded.roles && Array.isArray(decoded.roles)) {
                        console.log('Roles detectados:', decoded.roles);
                        return decoded.roles;
                    }
                }
            } catch (e) {
                console.error('Error decodificando el token:', e);
            }
        }
        return [];
    }



    isWriterOrAdmin(): boolean {
        const roles = this.getRoles();
        const result = roles.includes('WRITER') || roles.includes('ADMIN');
        console.log('¿Writer o Admin?', result);
        return result;
    }


}