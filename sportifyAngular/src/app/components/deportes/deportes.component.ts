import { Component, OnInit } from '@angular/core';
import { Deporte, DeportePage } from '../../models/deporte/deporte.model';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { DeportesService } from '../../services/deportes.service';

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

    constructor(
        private deportesService: DeportesService,
        private authService: AuthService,
        private router: Router
    ) {}

    ngOnInit() {
        this.isLoggedIn = this.authService.isAuthenticated();
        this.cargarDeportesYSeguidos(0);
    }

    cargarDeportesYSeguidos(page: number) {
        if (this.isLoggedIn) {
            this.deportesService.getDeportesSeguidos().subscribe({
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
        this.deportesService.getDeportes(page, this.size).subscribe(resp => {
            this.deportes = resp.content || [];
            this.page = resp.number;
            this.totalPages = resp.totalPages;
        });
    }

    toggleFollow(deporte: Deporte, event: Event) {
        event.stopPropagation();
        const estabaSeguido = this.seguidoIds.has(deporte.id);
        const obs = estabaSeguido
            ? this.deportesService.unfollowDeporte(deporte.nombreNoEspacio)
            : this.deportesService.seguirDeporte(deporte.nombreNoEspacio);

        obs.subscribe({
            next: (resp: any) => {
                const deportesSeguidos = resp.deportesSeguidos || [];
                this.seguidoIds = new Set(deportesSeguidos.map((d: any) => d.id));
            },
            error: () => {
                const checkbox = event.target as HTMLInputElement;
                checkbox.checked = estabaSeguido;
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