import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Liga, LigaPage } from '../../models/liga/liga.model';

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

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('accessToken');
    this.route.queryParams.subscribe(params => {
      this.deporteFiltro = params['deporte'] || null;
      this.cargarLigasYLikes(0);
    });
  }

  cargarLigasYLikes(page: number) {
    if (this.isLoggedIn) {
      this.http.get<any>('/me').subscribe({
        next: resp => {
          // Busca ligas seguidas en el array ligasSeguidas de /me
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
      this.http.get<LigaPage>(`/liga/${this.deporteFiltro}?page=${page}&size=${this.size}`).subscribe(resp => {
        this.ligas = resp.content || [];
        this.page = resp.number;
        this.totalPages = resp.totalPages;
      });
    } else {
      this.http.get<LigaPage>(`/liga?page=${page}&size=${this.size}`).subscribe(resp => {
        this.ligas = resp.content || [];
        this.page = resp.number;
        this.totalPages = resp.totalPages;
      });
    }
  }

  toggleLike(liga: Liga, event: Event) {
    event.stopPropagation();
    const isLiked = this.ligaLikedIds.has(liga.id);

    if (isLiked) {
      // Eliminar like
      this.http.put('/unfollowLiga', { nombreLiga: liga.nombreSinEspacio }).subscribe({
        next: () => {
          this.ligaLikedIds.delete(liga.id);
        },
        error: () => {
          (event.target as HTMLInputElement).checked = true;
        }
      });
    } else {
      // Dar like
      this.http.put('/seguirLiga', { nombreLiga: liga.nombreSinEspacio }).subscribe({
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
