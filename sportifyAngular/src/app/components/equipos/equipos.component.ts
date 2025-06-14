import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Equipo, EquipoPage } from '../../models/equipo/equipo.model';



@Component({
  selector: 'app-equipos',
  templateUrl: './equipos.component.html',
  styleUrls: ['./equipos.component.css']
})
export class EquiposComponent implements OnInit {
  equipos: Equipo[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  ligaFiltro: string | null = null;
  equipoLikedIds: Set<string> = new Set();
  isLoggedIn = false;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('accessToken');
    this.route.queryParams.subscribe(params => {
      this.ligaFiltro = params['liga'] || null;
      this.cargarEquiposYLikes(0);
    });
  }

  cargarEquiposYLikes(page: number) {
    if (this.isLoggedIn) {
      this.http.get<any>('/me').subscribe({
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
      this.http.get<EquipoPage>(`/equipo/${this.ligaFiltro}?page=${page}&size=${this.size}`).subscribe(resp => {
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
      // Eliminar like
      this.http.put('/unfollowEquipo',{ nombreEquipo: equipo.nombreNoEspacio }).subscribe({
        next: () => {
          this.equipoLikedIds.delete(equipo.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      // Dar like
      this.http.put('/seguirEquipo', { nombreEquipo: equipo.nombreNoEspacio }).subscribe({
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
