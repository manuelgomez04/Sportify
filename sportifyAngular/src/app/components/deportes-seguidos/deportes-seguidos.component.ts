import { Component, OnInit } from '@angular/core';
import { DeportesService } from '../../services/deportes.service';

@Component({
  selector: 'app-deportes-seguidos',
  templateUrl: './deportes-seguidos.component.html',
  styleUrl: './deportes-seguidos.component.css'
})
export class DeportesSeguidosComponent implements OnInit {
  deportes: any[] = [];
  page = 0;
  size = 4;
  totalPages = 0;

  constructor(private deportesService: DeportesService) { }

  ngOnInit() {
    this.cargarDeportes(0);
  }

  cargarDeportes(page: number) {
    this.deportesService.getDeportesFavoritos(page, this.size).subscribe({
      next: resp => {
        this.deportes = resp.deportesFavoritos?.content || [];
        this.page = resp.deportesFavoritos?.number || 0;
        this.totalPages = resp.deportesFavoritos?.totalPages || 0;
      }
    });
  }

  scrollLeft() {
    if (this.page > 0) {
      this.cargarDeportes(this.page - 1);
    }
  }
  scrollRight() {
    if (this.page < this.totalPages - 1) {
      this.cargarDeportes(this.page + 1);
    }
  }
  get visibleDeportes() {
    return this.deportes;
  }
  get atStart() {
    return this.page === 0;
  }
  get atEnd() {
    return this.page >= this.totalPages - 1 || this.totalPages === 0;
  }
}
