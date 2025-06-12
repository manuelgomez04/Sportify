import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrls: ['./verify-account.component.css']
})
export class VerifyAccountComponent {
  verifyForm: FormGroup;
  message: string | null = null;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.verifyForm = this.fb.group({
      token: ['', Validators.required]
    });
  }

  onSubmit() {
    this.message = null;
    this.error = null;
    if (this.verifyForm.valid) {
      this.authService.verifyAccount(this.verifyForm.value).subscribe({
        next: (res: any) => {
          this.message = 'Cuenta verificada correctamente. Ya puedes iniciar sesión.';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: err => {
          this.error = err.error?.message || 'Error al verificar la cuenta';
        }
      });
    }
  }
}
