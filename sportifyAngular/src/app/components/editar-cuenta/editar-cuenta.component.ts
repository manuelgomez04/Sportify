import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-editar-cuenta',
  templateUrl: './editar-cuenta.component.html',
  styleUrls: ['./editar-cuenta.component.css']
})
export class EditarCuentaComponent implements OnInit {
  editForm: FormGroup;
  success = false;
  error = '';
  fieldErrors: { [key: string]: string } = {};
  selectedProfileImage: File | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private usuarioService: UsuarioService
  ) {
    this.editForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      verifyEmail: ['', [Validators.required, Validators.email]],
      password: [''],
      verifyPassword: [''],
      profileImage: [null]
    });
  }

  ngOnInit() {
    this.usuarioService.getUsuario('').subscribe({
      next: (user) => {
        this.editForm.patchValue({
          nombre: user.email || '',
          email: user.nombre || '',
          verifyEmail: user.nombre || ''
        });
        this.editForm.get('nombre')?.markAsTouched();
        this.editForm.get('email')?.markAsTouched();
        this.editForm.get('verifyEmail')?.markAsTouched();
      }
    });
  }

  onProfileImageChange(event: any) {
    const file = event.target.files && event.target.files[0];
    if (file) {
      this.selectedProfileImage = file;
      this.editForm.patchValue({ profileImage: file });
    }
  }

  submit() {
    this.error = '';
    this.fieldErrors = {};

    if (
      this.editForm.invalid ||
      (this.editForm.value.password && this.editForm.value.password !== this.editForm.value.verifyPassword)
    ) return;

    const formData = new FormData();
    const userObj: any = { ...this.editForm.value };
    delete userObj.profileImage;
    formData.append(
      'editUserDto',
      new Blob([JSON.stringify(userObj)], { type: 'application/json' })
    );
    if (this.editForm.value.profileImage instanceof File) {
      formData.append('profileImage', this.editForm.value.profileImage);
    }

    this.usuarioService.editarUsuario(formData).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => this.router.navigate(['/me']), 1500);
      },
      error: err => {
        this.fieldErrors = {};
        const isPassayError = (msg: string) =>
          msg && (
            msg.includes('Password must') ||
            msg.includes('Password must be') ||
            msg.includes('Password must contain')
          );

        if (err.error) {
          if (err.error['invalid-params'] && Array.isArray(err.error['invalid-params'])) {
            err.error['invalid-params'].forEach((e: any) => {
              if (e.field) {
                this.fieldErrors[e.field] = e.message;
              }
              if (isPassayError(e.message)) {
                this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
              }
            });
          }
          else if (err.error.errors && Array.isArray(err.error.errors)) {
            err.error.errors.forEach((e: any) => {
              if (e.field) {
                this.fieldErrors[e.field] = e.defaultMessage || e.message || e;
              }
              if (isPassayError(e.defaultMessage || e.message || '')) {
                this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
              }
            });
          }
          else if (Array.isArray(err.error)) {
            err.error.forEach((e: any) => {
              if (e.field) {
                this.fieldErrors[e.field] = e.defaultMessage || e.message || e;
              }
              if (isPassayError(e.defaultMessage || e.message || '')) {
                this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
              }
            });
          }
          else if (typeof err.error === 'string' && isPassayError(err.error)) {
            this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
          }
          else if (err.error.message && isPassayError(err.error.message)) {
            this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
          }
          else if (err.status === 409 || (err.error.message && err.error.message.includes('email'))) {
            this.fieldErrors['email'] = 'El correo electrónico ya está en uso por otro usuario.';
          }
          else if (typeof err.error === 'string') {
            this.error = err.error;
          }
          else if (err.error.message) {
            this.error = err.error.message;
          }
          else {
            this.error = 'Error al actualizar la cuenta';
          }
        } else {
          this.error = 'Error al actualizar la cuenta';
        }
      }
    });
  }
}
