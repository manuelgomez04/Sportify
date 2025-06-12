import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { userLogin } from '../models/user/model-login';
import { miUsuario } from '../models/user/miUsuario';
import { UserRegister } from '../models/user/model-register';
import { ActivateAccount } from '../models/user/model-activate-account';


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

    register(userData: UserRegister, type: 'user' | 'writer') {
        const url = type === 'writer' ? '/writer/auth/register' : '/user/auth/register';
        return this.http.post(url, userData);
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('accessToken');
    }

    getAuthorizationHeader(): string {
        return 'Bearer ' + localStorage.getItem('accessToken');
    }

    verifyAccount(body: { token: string }) {
        return this.http.post('/activate/account/', body);
     
    }
}

