import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Deporte, DeportePage } from '../../models/deporte/deporte.model';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';



@Component({
    selector: 'app-deportes',
    templateUrl: './deportes.component.html',
    styleUrls: ['./deportes.component.css']
})
export class DeportesComponent implements OnInit {
    deportes: Deporte[] = [];
    page = 0;
    size = 10;
    totalPages = 0;
    isLoggedIn = false;
    seguidoIds: Set<string> = new Set();

    constructor(private http: HttpClient, private authService: AuthService, private router: Router) { }

    ngOnInit() {
        this.isLoggedIn = this.authService.isAuthenticated();
        this.cargarDeportesYSeguidos(0);
    }

    cargarDeportesYSeguidos(page: number) {
        if (this.isLoggedIn) {
            // Usa /me para obtener los deportes seguidos antes de cargar los deportes
            this.http.get<any>('/me').subscribe({
                next: resp => {
                    const deportesSeguidos = resp.deportesSeguidos || [];
                    this.seguidoIds = new Set(deportesSeguidos.map((d: any) => d.id));
                    this.cargarDeportes(page);
                },
                error: () => {
                    this.seguidoIds = new Set();
                    this.cargarDeportes(page);
                }
            });
        } else {
            this.seguidoIds = new Set();
            this.cargarDeportes(page);
        }
    }

    cargarDeportes(page: number) {
        this.http.get<DeportePage>(`/deporte?page=${page}&size=${this.size}`).subscribe(resp => {
            this.deportes = resp.content || [];
            this.page = resp.number;
            this.totalPages = resp.totalPages;
            // Si tienes la info de seguidos en el usuario autenticado, actualiza seguidoIds aquí
            // Por ejemplo, si tu backend devuelve los deportes seguidos en /me o similar, haz una petición aquí y actualiza seguidoIds
        });
    }

    toggleFollow(deporte: Deporte, event: Event) {
        event.stopPropagation();

        const estabaSeguido = this.seguidoIds.has(deporte.id);
        const endpoint = estabaSeguido ? '/unfollowDeporte' : '/seguirDeporte';

        this.http.put(endpoint, { nombreDeporte: deporte.nombreNoEspacio }).subscribe({
            next: (resp: any) => {
                const deportesSeguidos = resp.deportesSeguidos || [];
                this.seguidoIds = new Set(deportesSeguidos.map((d: any) => d.id));
            },
            error: () => {
                // En caso de error, revertir el checkbox
                const checkbox = event.target as HTMLInputElement;
                checkbox.checked = estabaSeguido; // Vuelve al estado anterior
            }
        });
    }

    cambiarPagina(p: number) {
        if (p >= 0 && p < this.totalPages) {
            this.cargarDeportesYSeguidos(p);
        }
    }

    verLigasDeporte(nombreNoEspacio: string) {
        this.router.navigate(['/ligas'], { queryParams: { deporte: nombreNoEspacio } });
    }
}