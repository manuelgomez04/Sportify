import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { userLogin } from '../models/user/model-login';
import { miUsuario } from '../models/user/miUsuario';
import { UserRegister } from '../models/user/model-register';
import { ActivateAccount } from '../models/user/model-activate-account';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private authStateSubject = new BehaviorSubject<boolean>(this.isAuthenticated());
    authState$ = this.authStateSubject.asObservable();

    constructor(private http: HttpClient) { }

    userLogin(body: { username: string; password: string }): Observable<userLogin> {
        return this.http.post<userLogin>('/auth/login', body);
    }
    getUsuario(username: string) {
        return this.http.get<miUsuario>(`/me`);
    }
    logout() {
        if (localStorage.getItem('accessToken')) {
            this.http.post('/auth/logout', {}).subscribe({
                next: () => {
                    localStorage.clear();
                    this.authStateSubject.next(false);
                    window.location.reload(); // Refresca la página tras logout
                },
                error: () => {
                    localStorage.clear();
                    this.authStateSubject.next(false);
                    window.location.reload(); // Refresca la página tras logout
                }
            });
        } else {
            localStorage.clear();
            this.authStateSubject.next(false);
            window.location.reload(); // Refresca la página tras logout
        }
    }

    register(data: any, userType: string) {
        let url = userType === 'writer' ? '/writer/auth/register' : '/user/auth/register';
       
        if (data instanceof FormData) {
            return this.http.post(url, data); 
        } else {
            return this.http.post(url, data, { headers: { 'Content-Type': 'application/json' } });
        }
    }

    isAuthenticated(): boolean {
        // Mejor comprobar el token o el usuario real
        return !!localStorage.getItem('accessToken') || !!localStorage.getItem('user');
    }

    getAuthorizationHeader(): string {
        return 'Bearer ' + localStorage.getItem('accessToken');
    }

    verifyAccount(body: { token: string }) {
        return this.http.post('/activate/account/', body);

    }

    getRoles(): string[] {
        const userStr = localStorage.getItem('user') || sessionStorage.getItem('user');
        if (userStr) {
            try {
                const user = JSON.parse(userStr);
                if (user.token) {
                    const decoded: any = jwtDecode(user.token);
                    if (decoded.roles && Array.isArray(decoded.roles)) {
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
        return result;
    }

    isAdmin(): boolean {
        const roles = this.getRoles();
        return roles.includes('ADMIN');
    }

    loginSuccess() {
        this.authStateSubject.next(true);
    }
}