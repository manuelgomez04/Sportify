import { Component, OnInit } from '@angular/core';
import { LigasService } from '../../services/ligas.service';

@Component({
  selector: 'app-ligas-seguidas',
  templateUrl: './ligas-seguidas.component.html',
  styleUrls: ['./ligas-seguidas.component.css']
})
export class LigasSeguidasComponent implements OnInit {
  ligas: any[] = [];
  page = 0;
  size = 4;
  totalPages = 0;

  constructor(private ligasService: LigasService) {}

  ngOnInit() {
    this.cargarLigas(0);
  }

  cargarLigas(page: number) {
    this.ligasService.getLigasFavoritas(page, this.size).subscribe({
      next: resp => {
        this.ligas = resp.ligasFavoritas?.content || [];
        this.page = resp.ligasFavoritas?.number || 0;
        this.totalPages = resp.ligasFavoritas?.totalPages || 0;
      }
    });
  }

  scrollLeft() {
    if (this.page > 0) {
      this.cargarLigas(this.page - 1);
    }
  }

  scrollRight() {
    if (this.page < this.totalPages - 1) {
      this.cargarLigas(this.page + 1);
    }
  }

  get visibleLigas() {
    return this.ligas;
  }

  get atStart() {
    return this.page === 0;
  }

  get atEnd() {
    return this.page >= this.totalPages - 1 || this.totalPages === 0;
  }
}
