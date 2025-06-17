import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioAdmin } from '../models/user/usuario-admin.model';
import { EditUserRequest } from '../models/user/edit-user.model';
import { RegisterAdminResponse } from '../models/user/register-admin';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UsuarioAdmin[]> {
    return this.http.get<UsuarioAdmin[]>('/users/all');
  }

  editUser(username: string, data: EditUserRequest) {
    return this.http.put(`/edit/${username}`, data);
  }

  deleteUser(username: string) {
    return this.http.delete(`/admin/delete/${username}`);
  }

  registerAdmin(formData: FormData): Observable<RegisterAdminResponse> {
  return this.http.post<RegisterAdminResponse>('/admin/auth/register', formData);
}
  activateAccount(data: { token: string }) {
  return this.http.post('/activate/account/', data);
}
}
