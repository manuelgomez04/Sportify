import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UsuarioAdmin } from '../../models/user/usuario-admin.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EditUserRequest } from '../../models/user/edit-user.model';

@Component({
  selector: 'app-admin-usuarios',
  templateUrl: './admin-usuarios.component.html',
  styleUrls: ['./admin-usuarios.component.css']
})
export class AdminUsuariosComponent implements OnInit {
  usuarios: any[] = [];
  filtro: string = '';
  editando: string | null = null;
  editForm!: FormGroup;
  usuarioAEliminar: UsuarioAdmin | null = null;
  deleteError: string | null = null;
  deleteLoading: string | null = null;

  constructor(
    private userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe(users => {
      this.usuarios = users;
    });
  }

  get usuariosFiltrados() {
    if (!this.filtro.trim()) return this.usuarios;
    const f = this.filtro.toLowerCase();
    return this.usuarios.filter(u =>
      u.nombre?.toLowerCase().includes(f) ||
      u.email?.toLowerCase().includes(f) ||
      u.username?.toLowerCase().includes(f)
    );
  }

  abrirEditar(usuario: UsuarioAdmin) {
    this.editando = usuario.username;
    this.editForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      verifyPassword: ['', [Validators.required]],
      email: [usuario.nombre, [Validators.required, Validators.email]], // email real
      verifyEmail: [usuario.nombre, [Validators.required, Validators.email]], // email real
      nombre: [usuario.email, [Validators.required]] // nombre real
    });
  }

  cerrarEditar() {
    this.editando = null;
  }

  guardarEdicion(usuario: UsuarioAdmin) {
    if (this.editForm.invalid) return;
    const data: EditUserRequest = this.editForm.value;
    this.userService.editUser(usuario.username, data).subscribe({
      next: updated => {
        usuario.nombre = data.email;
        usuario.email = data.nombre;
        this.cerrarEditar();
      },
      error: err => {
        // Maneja el error según tu UX
      }
    });
  }

  abrirEliminar(usuario: UsuarioAdmin) {
    this.usuarioAEliminar = usuario;
    this.deleteError = null;
  }

  cerrarEliminar() {
    this.usuarioAEliminar = null;
    this.deleteError = null;
  }

  eliminarUsuarioConfirmado() {
    if (!this.usuarioAEliminar) return;
    this.deleteLoading = this.usuarioAEliminar.username;
    this.deleteError = null;
    this.userService.deleteUser(this.usuarioAEliminar.username).subscribe({
      next: () => {
        this.usuarios = this.usuarios.filter(u => u.username !== this.usuarioAEliminar?.username);
        this.cerrarEliminar();
      },
      error: () => {
        this.deleteError = 'Error al eliminar el usuario';
      },
      complete: () => {
        this.deleteLoading = null;
      }
    });
  }
}

