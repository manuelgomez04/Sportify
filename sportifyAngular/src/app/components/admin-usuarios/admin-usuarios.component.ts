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
  showAddAdminModal = false;
  addAdminForm!: FormGroup;
  addAdminLoading = false;
  addAdminError: string | null = null;
  addAdminSuccess: boolean = false;
  addAdminImage: File | null = null;
  showAddAdminPassword = false;
  showAddAdminVerifyPassword = false;
  page = 0;
  pageSize = 10;
  totalPages = 0;

  constructor(
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.addAdminForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]],
      verifyPassword: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      verifyEmail: ['', [Validators.required, Validators.email]],
      fechaNacimiento: ['', Validators.required],
      nombre: ['', Validators.required],
      profileImage: [null]
    });
  }

  ngOnInit() {
    this.userService.getAllUsers().subscribe(users => {
      this.usuarios = users;
      this.totalPages = Math.ceil(this.usuariosFiltrados.length / this.pageSize);
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

  get usuariosPaginados() {
    const start = this.page * this.pageSize;
    return this.usuariosFiltrados.slice(start, start + this.pageSize);
  }

  cambiarPagina(p: number) {
    if (p >= 0 && p < this.totalPages) {
      this.page = p;
    }
  }

  ngDoCheck() {
    const newTotalPages = Math.ceil(this.usuariosFiltrados.length / this.pageSize);
    if (this.totalPages !== newTotalPages) {
      this.totalPages = newTotalPages;
      if (this.page >= this.totalPages) {
        this.page = 0;
      }
    }
  }

  abrirEditar(usuario: UsuarioAdmin) {
    this.editando = usuario.username;
    this.editForm = this.fb.group({
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

  abrirAddAdmin() {
    this.addAdminForm.reset();
    this.addAdminImage = null;
    this.addAdminError = null;
    this.addAdminSuccess = false;
    this.showAddAdminModal = true;
  }

  cerrarAddAdmin() {
    this.showAddAdminModal = false;
    this.addAdminError = null;
    this.addAdminSuccess = false;
    this.addAdminImage = null;
  }

  onAddAdminImageSelected(event: any) {
    const file = event.target.files && event.target.files[0];
    if (file) {
      this.addAdminImage = file;
      this.addAdminForm.patchValue({ profileImage: file });
    } else {
      this.addAdminImage = null;
      this.addAdminForm.patchValue({ profileImage: null });
    }
  }

  crearAdmin() {
    if (this.addAdminForm.invalid) {
      this.addAdminError = 'Completa todos los campos obligatorios.';
      return;
    }
    this.addAdminLoading = true;
    this.addAdminError = null;
    this.addAdminSuccess = false;

    const formData = new FormData();
    const createUserRequest = {
      username: this.addAdminForm.value.username,
      password: this.addAdminForm.value.password,
      verifyPassword: this.addAdminForm.value.verifyPassword,
      email: this.addAdminForm.value.email,
      verifyEmail: this.addAdminForm.value.verifyEmail,
      fechaNacimiento: this.addAdminForm.value.fechaNacimiento,
      nombre: this.addAdminForm.value.nombre
    };
    formData.append('createUserRequest', new Blob([JSON.stringify(createUserRequest)], { type: 'application/json' }));
    if (this.addAdminImage) {
      formData.append('profileImage', this.addAdminImage);
    }

    this.userService.registerAdmin(formData).subscribe({
      next: () => {
        this.addAdminSuccess = true;
        this.addAdminLoading = false;
        setTimeout(() => {
          this.cerrarAddAdmin();
          window.location.reload();
        }, 1200);
      },
      error: (err) => {
        this.addAdminError = 'Error al crear el admin';
        this.addAdminLoading = false;
      }
    });
  }
}

