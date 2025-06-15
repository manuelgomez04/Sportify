import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
  constructor(private router: Router) {}

  goToUsuarios() {
    this.router.navigate(['/admin/usuarios']);
  }
  goToLigas() {
    this.router.navigate(['/admin/ligas']);
  }
  goToDeportes() {
    this.router.navigate(['/admin/deportes']);
  }
  goToEquipos() {
    this.router.navigate(['/admin/equipos']);
  }
}
