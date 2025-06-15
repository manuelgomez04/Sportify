import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Liga, LigaPage } from '../../models/liga/liga.model';
import { LigasService } from '../../services/ligas.service';

@Component({
  selector: 'app-ligas',
  templateUrl: './ligas.component.html',
  styleUrls: ['./ligas.component.css']
})
export class LigasComponent implements OnInit {
  ligas: Liga[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  ligaLikedIds: Set<string> = new Set();
  deporteFiltro: string | null = null;
  isLoggedIn = false;
  descExpandida: boolean[] = []; 

  constructor(
    private ligasService: LigasService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('accessToken');
    this.route.queryParams.subscribe(params => {
      this.deporteFiltro = params['deporte'] || null;
      this.cargarLigasYLikes(0);
    });
  }

  cargarLigasYLikes(page: number) {
    if (this.isLoggedIn) {
      this.ligasService.getLigasSeguidas().subscribe({
        next: resp => {
          const ligasSeguidas = resp.ligasSeguidas || [];
          this.ligaLikedIds = new Set(ligasSeguidas.map((l: any) => l.id));
          this.cargarLigas(page);
        },
        error: () => {
          this.ligaLikedIds = new Set();
          this.cargarLigas(page);
        }
      });
    } else {
      this.ligaLikedIds = new Set();
      this.cargarLigas(page);
    }
  }

  cargarLigas(page: number) {
    if (this.deporteFiltro) {
      this.ligasService.getLigasPorDeporte(this.deporteFiltro, page, this.size).subscribe(resp => {
        this.ligas = resp.content || [];
        this.page = resp.number;
        this.totalPages = resp.totalPages;
        this.descExpandida = new Array(this.ligas.length).fill(false); // reset
      });
    } else {
      this.ligasService.getLigas(page, this.size).subscribe(resp => {
        this.ligas = resp.content || [];
        this.page = resp.number;
        this.totalPages = resp.totalPages;
        this.descExpandida = new Array(this.ligas.length).fill(false); // reset
      });
    }
  }

  toggleLike(liga: Liga, event: Event) {
    event.stopPropagation();
    const isLiked = this.ligaLikedIds.has(liga.id);

    if (isLiked) {
      this.ligasService.unfollowLiga(liga.nombreSinEspacio).subscribe({
        next: () => {
          this.ligaLikedIds.delete(liga.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      this.ligasService.seguirLiga(liga.nombreSinEspacio).subscribe({
        next: () => {
          this.ligaLikedIds.add(liga.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = false;
        }
      });
    }
  }

  cambiarPagina(p: number) {
    if (p >= 0 && p < this.totalPages) {
      this.cargarLigasYLikes(p);
    }
  }

  verEquiposLiga(nombreLiga: string) {
    this.router.navigate(['/equipos'], { queryParams: { liga: nombreLiga } });
  }
}
