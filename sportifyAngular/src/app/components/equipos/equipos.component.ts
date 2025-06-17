import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Equipo, EquipoPage } from '../../models/equipo/equipo.model';
import { EquiposService } from '../../services/equipos.service';

@Component({
  selector: 'app-equipos',
  templateUrl: './equipos.component.html',
  styleUrls: ['./equipos.component.css']
})
export class EquiposComponent implements OnInit {
  equipos: Equipo[] = [];
  page = 0;
  size = 5;
  totalPages = 0;
  ligaFiltro: string | null = null;
  equipoLikedIds: Set<string> = new Set();
  isLoggedIn = false;

  constructor(private equiposService: EquiposService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('accessToken');
    this.route.queryParams.subscribe(params => {
      this.ligaFiltro = params['liga'] || null;
      this.cargarEquiposYLikes(0);
    });
  }

  cargarEquiposYLikes(page: number) {
    if (this.isLoggedIn) {
      this.equiposService.getEquiposSeguidos().subscribe({
        next: resp => {
          const equiposSeguidos = resp.equiposSeguidos || [];
          this.equipoLikedIds = new Set(equiposSeguidos.map((e: any) => e.id));
          this.cargarEquipos(page);
        },
        error: () => {
          this.equipoLikedIds = new Set();
          this.cargarEquipos(page);
        }
      });
    } else {
      this.equipoLikedIds = new Set();
      this.cargarEquipos(page);
    }
  }

  cargarEquipos(page: number) {
    if (this.ligaFiltro) {
      this.equiposService.getEquiposPorLiga(this.ligaFiltro, page, this.size).subscribe(resp => {
        this.equipos = resp.content || [];
        this.page = resp.number;
        this.totalPages = resp.totalPages;
      });
    }
  }

  toggleLike(equipo: Equipo, event: Event) {
    event.stopPropagation();
    const isLiked = this.equipoLikedIds.has(equipo.id);

    if (isLiked) {
      this.equiposService.unfollowEquipo(equipo.nombreNoEspacio).subscribe({
        next: () => {
          this.equipoLikedIds.delete(equipo.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      this.equiposService.seguirEquipo(equipo.nombreNoEspacio).subscribe({
        next: () => {
          this.equipoLikedIds.add(equipo.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = false;
        }
      });
    }
  }

  cambiarPagina(p: number) {
    if (p >= 0 && p < this.totalPages) {
      this.cargarEquiposYLikes(p);
    }
  }
}
