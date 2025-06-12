import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { EditUser } from '../../models/user/edit-user.model';
import { AuthService } from '../../services/auth.service';

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

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.editForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      verifyEmail: ['', [Validators.required, Validators.email]],
      password: [''],
      verifyPassword: ['']
    });
  }

  ngOnInit() {
    this.authService.getUsuario('').subscribe({
      next: (user) => {
        this.editForm.patchValue({
          nombre: user.email || '',
          email: user.nombre || '',
          verifyEmail: user.nombre || ''
        });
      }
    });
  }

  submit() {
    this.error = '';
    this.fieldErrors = {};

    if (
      this.editForm.invalid ||
      (this.editForm.value.password && this.editForm.value.password !== this.editForm.value.verifyPassword)
    ) return;

    const body: any = { ...this.editForm.value };

    if (!body.password) {
      delete body.password;
      delete body.verifyPassword;
    }

    this.http.put('/edit/me', body).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => this.router.navigate(['/me']), 1500);
      },
      error: err => {
        this.fieldErrors = {};
        // Passay: errores de contraseña en inglés, suelen contener "Password must"
        const isPassayError = (msg: string) =>
          msg && (
            msg.includes('Password must') ||
            msg.includes('Password must be') ||
            msg.includes('Password must contain')
          );

        if (err.error) {
          // invalid-params (RFC 7807)
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
          // Spring Validation: errors: [{field: "...", defaultMessage: "..."}]
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
          // Array de errores
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
          // Mensaje simple (string)
          else if (typeof err.error === 'string' && isPassayError(err.error)) {
            this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
          }
          // Mensaje en 'message'
          else if (err.error.message && isPassayError(err.error.message)) {
            this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
          }
          // Email duplicado
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
