import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UserRegister } from '../../models/user/model-register';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  error: string | null = null;
  userType: 'user' | 'writer' = 'user';
  fieldErrors: { [key: string]: string } = {};
  selectedProfileImage: File | null = null;


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      verifyEmail: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      verifyPassword: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      profileImage: [null]
    });
  }

  onProfileImageChange(event: any) {
    const file = event.target.files && event.target.files[0];
    if (file) {
      this.selectedProfileImage = file;
      this.registerForm.patchValue({ profileImage: file });
    }
  }

  onSubmit() {
    this.error = null;
    this.fieldErrors = {};
    if (this.registerForm.valid) {
      const formData = new FormData();
      // Construye el objeto de usuario (sin el archivo)
      const userObj: any = { ...this.registerForm.value };
      delete userObj.profileImage;
      // Añade el objeto como JSON en el campo que espera el backend, usando Blob para el tipo correcto
      formData.append(
        'createUserRequest',
        new Blob([JSON.stringify(userObj)], { type: 'application/json' })
      );
      // Añade el archivo si existe, usando el nombre original del archivo
      if (this.registerForm.value.profileImage instanceof File) {
        const file: File = this.registerForm.value.profileImage;
        formData.append('profileImage', file, file.name || 'profileImage.jpg');
      }
      this.authService.register(formData, this.userType).subscribe({
        next: () => this.router.navigate(['/verify-account']),
        error: err => {
          this.fieldErrors = {};
          if (err.error) {
            // Passay: errores de contraseña en inglés, suelen contener "Password must"
            const isPassayError = (msg: string) =>
              msg.includes('Password must') ||
              msg.includes('Password must be') ||
              msg.includes('Password must contain');

            // invalid-params (RFC 7807)
            if (err.error['invalid-params'] && Array.isArray(err.error['invalid-params'])) {
              err.error['invalid-params'].forEach((e: any) => {
                if (e.field === 'password' && isPassayError(e.message)) {
                  this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else if (e.field) {
                  this.fieldErrors[e.field] = e.message;
                } else if (isPassayError(e.message)) {
                  this.error = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else {
                  this.error = (this.error ? this.error + '<br>' : '') + (e.message || e);
                }
              });
            }
            // Spring Validation: errors: [{field: "...", defaultMessage: "..."}]
            else if (err.error.errors && Array.isArray(err.error.errors)) {
              err.error.errors.forEach((e: any) => {
                if (e.field === 'password' && isPassayError(e.defaultMessage || e.message || '')) {
                  this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else if (e.field) {
                  this.fieldErrors[e.field] = e.defaultMessage || e.message || e;
                } else if (isPassayError(e.defaultMessage || e.message || '')) {
                  this.error = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else {
                  this.error = (this.error ? this.error + '<br>' : '') + (e.defaultMessage || e.message || e);
                }
              });
            }
            // Array de errores
            else if (Array.isArray(err.error)) {
              err.error.forEach((e: any) => {
                if (e.field === 'password' && isPassayError(e.defaultMessage || e.message || '')) {
                  this.fieldErrors['password'] = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else if (e.field) {
                  this.fieldErrors[e.field] = e.defaultMessage || e.message || e;
                } else if (isPassayError(e.defaultMessage || e.message || '')) {
                  this.error = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
                } else {
                  this.error = (this.error ? this.error + '<br>' : '') + (e.defaultMessage || e.message || e);
                }
              });
            }
            // Mensaje simple (string)
            else if (typeof err.error === 'string' && isPassayError(err.error)) {
              this.error = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
            }
            // Mensaje en 'message'
            else if (err.error.message && isPassayError(err.error.message)) {
              this.error = 'La contraseña debe tener mínimo 8 caracteres, una mayúscula y un caracter especial';
            }
            else if (typeof err.error === 'string') {
              this.error = err.error;
            }
            else if (err.error.message) {
              this.error = err.error.message;
            }
            else {
              this.error = 'Error en el registro';
            }
          } else {
            this.error = 'Error en el registro';
          }
        }
      });
    }
  }
}

