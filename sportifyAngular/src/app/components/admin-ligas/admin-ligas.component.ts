import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LigasService } from '../../services/ligas.service';
import { DeportesService } from '../../services/deportes.service';
import { Liga } from '../../models/liga/liga.model';
import { Deporte } from '../../models/deporte/deporte.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
    selector: 'app-admin-ligas',
    templateUrl: './admin-ligas.component.html',
    styleUrls: ['./admin-ligas.component.css']
})
export class AdminLigasComponent implements OnInit {
    ligas: Liga[] = [];
    deportes: Deporte[] = [];
    displayedColumns: string[] = ['nombre', 'deporte', 'descripcion', 'acciones'];
    dataSource = new MatTableDataSource<Liga>([]);
    deporteFiltro: string = '';
    showDeleteModal = false;
    showAddModal = false;
    ligaAEliminar: any = null;
    deleteLoading: string | null = null;
    deleteError: string | null = null;
    addLigaForm: FormGroup;
    addLoading = false;
    addError: string | null = null;
    imagenFile: File | null = null;
    MAX_FILE_SIZE_MB = 5;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        private ligasService: LigasService,
        private deportesService: DeportesService,
        private fb: FormBuilder
    ) {
        this.addLigaForm = this.fb.group({
            nombre: ['', Validators.required],
            descripcion: ['', Validators.required],
            deporte: ['', Validators.required],
            imagen: [null, Validators.required]
        });
    }

    ngOnInit() {
        this.deportesService.getDeportes(0, 100).subscribe(resp => {
            this.deportes = resp.content || [];
            this.cargarLigas();
        });
    }

    cargarLigas() {
        this.ligasService.getAllLigas().subscribe(resp => {
            this.ligas = resp || [];
            this.dataSource.data = this.ligas;
            setTimeout(() => {
                if (this.paginator) this.dataSource.paginator = this.paginator;
                if (this.sort) this.dataSource.sort = this.sort;
            });
            this.applyFilter();
        });
    }

    applyFilter() {
        this.dataSource.filterPredicate = (data: Liga, filter: string) => {
            if (!filter) return true;

            const deporteObj = this.deportes.find(d => d.nombreNoEspacio === filter);
            if (!deporteObj) return false;

            return (
                data.deporteNoEspacio === deporteObj.nombre ||
                data.deporteNoEspacio === deporteObj.nombreNoEspacio
            );
        };
        this.dataSource.filter = this.deporteFiltro;
        this.dataSource.paginator?.firstPage();
    }

    onDeporteFilterChange() {
        this.applyFilter();
    }

    abrirEliminar(liga: any) {
        this.ligaAEliminar = liga;
        this.deleteError = null;
        this.showDeleteModal = true;
    }

    cerrarEliminar() {
        this.ligaAEliminar = null;
        this.deleteError = null;
        this.showDeleteModal = false;
    }

    eliminarLiga(nombreSinEspacio: string) {
        const liga = this.ligas.find(l => l.nombreSinEspacio === nombreSinEspacio);
        if (liga) {
            this.abrirEliminar(liga);
        }
    }

    eliminarLigaConfirmada() {
        if (!this.ligaAEliminar) return;
        this.deleteLoading = this.ligaAEliminar.nombreSinEspacio;
        this.deleteError = null;
        this.ligasService.eliminarLiga(this.ligaAEliminar.nombreSinEspacio).subscribe({
            next: () => {
                this.cerrarEliminar();
                this.cargarLigas();
            },
            error: () => {
                this.deleteError = 'Error al eliminar la liga';
            },
            complete: () => {
                this.deleteLoading = null;
            }
        });
    }

    abrirAddLiga() {
        this.addLigaForm.reset();
        this.addError = null;
        this.showAddModal = true;
    }

    cerrarAddLiga() {
        this.showAddModal = false;
        this.addError = null;
    }

    onImagenSelected(event: any) {
        const file = event.target.files && event.target.files[0];
        if (file) {
            if (file.size > this.MAX_FILE_SIZE_MB * 1024 * 1024) {
                this.addError = 'Archivo demasiado pesado';
                this.imagenFile = null;
                this.addLigaForm.get('imagen')?.setErrors({ fileSize: true });
                this.addLoading = false;
            } else {
                this.imagenFile = file;
                this.addLigaForm.patchValue({ imagen: file });
                this.addLigaForm.get('imagen')?.setErrors(null);
                this.addError = null;
                this.addLoading = false;
            }
        } else {
            this.imagenFile = null;
            this.addLigaForm.patchValue({ imagen: null });
            this.addLigaForm.get('imagen')?.setErrors({ required: true });
            this.addError = null;
            this.addLoading = false;
        }
    }

    crearLiga() {
        if (this.addLigaForm.invalid || !this.imagenFile) {
            if (this.addLigaForm.get('imagen')?.hasError('fileSize')) {
                this.addError = 'Archivo demasiado pesado';
            } else {
                this.addError = 'Completa todos los campos y selecciona una imagen';
            }
            this.addLoading = false;
            return;
        }
        if (this.imagenFile.size > this.MAX_FILE_SIZE_MB * 1024 * 1024) {
            this.addError = 'Archivo demasiado pesado';
            this.addLoading = false;
            return;
        }
        this.addLoading = true;
        this.addError = null;

        const formData = new FormData();
        const dto = {
            nombre: this.addLigaForm.value.nombre,
            descripcion: this.addLigaForm.value.descripcion,
            nombreDeporte: this.addLigaForm.value.deporte
        };
        formData.append('createLigaRequest', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
        formData.append('imagen', this.imagenFile);

        this.ligasService.crearLiga(formData).subscribe({
            next: () => {
                this.cerrarAddLiga();
                this.cargarLigas();
            },
            error: (err) => {
                if (err.status === 413) {
                    this.addError = 'Archivo demasiado pesado';
                } else {
                    this.addError = 'Error al crear la liga';
                }
                this.addLoading = false;
            },
            complete: () => {
                this.addLoading = false;
            }
        });
    }
}
