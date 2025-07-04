import { Component, OnInit } from '@angular/core';
import { EquiposService } from '../../services/equipos.service';

@Component({
  selector: 'app-equipos-seguidos',
  templateUrl: './equipos-seguidos.component.html',
  styleUrl: './equipos-seguidos.component.css'
})
export class EquiposSeguidosComponent implements OnInit {

  equipos: any[] = [];
  page = 0;
  size = 4;
  totalPages = 0;

  constructor(private equiposService: EquiposService) { }

  ngOnInit() {
    this.cargarEquipos(0);
  }

  cargarEquipos(page: number) {
    this.equiposService.getEquiposFavoritos(page, this.size).subscribe({
      next: resp => {
        this.equipos = resp.equiposFavoritos?.content || [];
        this.page = resp.equiposFavoritos?.number || 0;
        this.totalPages = resp.equiposFavoritos?.totalPages || 0;
      }
    });
  }

  scrollLeft() {
    if (this.page > 0) {
      this.cargarEquipos(this.page - 1);
    }
  }
  scrollRight() {
    if (this.page < this.totalPages - 1) {
      this.cargarEquipos(this.page + 1);
    }
  }
  get visibleEquipos() {
    return this.equipos;
  }
  get atStart() {
    return this.page === 0;
  }
  get atEnd() {
    return this.page >= this.totalPages - 1 || this.totalPages === 0;
  }
}
