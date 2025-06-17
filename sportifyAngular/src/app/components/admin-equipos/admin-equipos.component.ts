import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EquiposService } from '../../services/equipos.service';
import { LigasService } from '../../services/ligas.service';
import { Liga } from '../../models/liga/liga.model';
import { Equipo } from '../../models/equipo/equipo.model';

@Component({
  selector: 'app-admin-equipos',
  templateUrl: './admin-equipos.component.html',
  styleUrls: ['./admin-equipos.component.css']
})
export class AdminEquiposComponent implements OnInit {
  equipos: Equipo[] = [];
  ligas: Liga[] = [];
  filtroNombre: string = '';
  filtroLiga: string = '';
  showDeleteModal = false;
  showAddModal = false;
  equipoAEliminar: any = null;
  deleteLoading: string | null = null;
  deleteError: string | null = null;
  addEquipoForm: FormGroup;
  addLoading = false;
  addError: string | null = null;
  escudoFile: File | null = null;
  MAX_FILE_SIZE_MB = 5;
  page = 1;
  pageSize = 10;

  constructor(
    private equiposService: EquiposService,
    private ligasService: LigasService,
    private fb: FormBuilder
  ) {
    this.addEquipoForm = this.fb.group({
      nombre: ['', Validators.required],
      nombreLiga: ['', Validators.required],
      ciudad: ['', Validators.required],
      pais: ['', Validators.required],
      fechaCreacion: ['', Validators.required],
      escudo: [null, Validators.required]
    });
  }

  ngOnInit() {
    this.cargarEquipos();
    this.cargarLigas();
  }

  cargarLigas() {
    this.ligasService.getAllLigas().subscribe(resp => {
      this.ligas = resp || [];
    });
  }

  cargarEquipos() {
    this.equiposService.getAllEquiposSinPaginacion().subscribe(resp => {
      this.equipos = resp || [];
    });
  }

  abrirEliminar(equipo: any) {
    this.equipoAEliminar = equipo;
    this.deleteError = null;
    this.showDeleteModal = true;
  }

  cerrarEliminar() {
    this.equipoAEliminar = null;
    this.deleteError = null;
    this.showDeleteModal = false;
  }

  eliminarEquipo(nombreNoEspacio: string) {
    const equipo = this.equipos.find(e => e.nombreNoEspacio === nombreNoEspacio);
    if (equipo) {
      this.abrirEliminar(equipo);
    }
  }

  eliminarEquipoConfirmado() {
    if (!this.equipoAEliminar) return;
    this.deleteLoading = this.equipoAEliminar.nombreNoEspacio;
    this.deleteError = null;
    this.equiposService.eliminarEquipo(this.equipoAEliminar.nombreNoEspacio).subscribe({
      next: () => {
        this.cerrarEliminar();
        this.cargarEquipos();
      },
      error: (err) => {
        if (err.status === 409 || (err.error && err.error.message && err.error.message.includes('violates foreign key constraint'))) {
          this.deleteError = 'No se puede eliminar el equipo porque tiene usuarios asociados.';
        } else {
          this.deleteError = 'Error al eliminar el equipo';
        }
        this.deleteLoading = null;
      },
      complete: () => {
        this.deleteLoading = null;
      }
    });
  }

  abrirAddEquipo() {
    this.addEquipoForm.reset();
    this.escudoFile = null;
    this.addError = null;
    this.cargarLigas();
    this.showAddModal = true;
    // Asegúrate de que el formulario se actualiza correctamente
    setTimeout(() => this.addEquipoForm.updateValueAndValidity());
  }

  cerrarAddEquipo() {
    this.showAddModal = false;
    this.addError = null;
    this.escudoFile = null;
  }

  onEscudoSelected(event: any) {
    const file = event.target.files && event.target.files[0];
    if (file) {
      if (file.size > this.MAX_FILE_SIZE_MB * 1024 * 1024) {
        this.addError = 'Archivo demasiado pesado';
        this.escudoFile = null;
        this.addEquipoForm.get('escudo')?.setErrors({ fileSize: true });
        this.addLoading = false;
      } else {
        this.escudoFile = file;
        this.addEquipoForm.patchValue({ escudo: file });
        this.addEquipoForm.get('escudo')?.setErrors(null);
        this.addError = null;
        this.addLoading = false;
      }
    } else {
      this.escudoFile = null;
      this.addEquipoForm.patchValue({ escudo: null });
      this.addEquipoForm.get('escudo')?.setErrors({ required: true });
      this.addError = null;
      this.addLoading = false;
    }
  }

  // Asegúrate de que el método crearEquipo está correctamente definido y no hay errores de sintaxis
  crearEquipo() {


    if (this.addEquipoForm.invalid || !this.escudoFile) {
      if (this.addEquipoForm.get('escudo')?.hasError('fileSize')) {
        this.addError = 'Archivo demasiado pesado';
      } else {
        this.addError = 'Completa todos los campos y selecciona un escudo';
      }
      this.addLoading = false;
      return;
    }
    if (this.escudoFile.size > this.MAX_FILE_SIZE_MB * 1024 * 1024) {
      this.addError = 'Archivo demasiado pesado';
      this.addLoading = false;
      return;
    }
    this.addLoading = true;
    this.addError = null;

    const formData = new FormData();
    const equipoDto = {
      nombre: this.addEquipoForm.value.nombre,
      nombreLiga: this.addEquipoForm.value.nombreLiga,
      ciudad: this.addEquipoForm.value.ciudad,
      pais: this.addEquipoForm.value.pais,
      fechaCreacion: this.addEquipoForm.value.fechaCreacion
    };
    formData.append('equipo', new Blob([JSON.stringify(equipoDto)], { type: 'application/json' }));
    formData.append('escudo', this.escudoFile);



    this.equiposService.crearEquipo(formData).subscribe({
      next: () => {
        this.cerrarAddEquipo();
        this.cargarEquipos();
      },
      error: (err) => {
        if (err.status === 413) {
          this.addError = 'Archivo demasiado pesado';
        } else {
          this.addError = 'Error al crear el equipo';
        }
        this.addLoading = false;
      },
      complete: () => {
        this.addLoading = false;
      }
    });
  }

  get equiposFiltrados() {
    let equipos = this.equipos;
    if (this.filtroNombre && this.filtroNombre.trim() !== '') {
      equipos = equipos.filter(e =>
        e.nombre?.toLowerCase().includes(this.filtroNombre.toLowerCase())
      );
    }
    if (this.filtroLiga && this.filtroLiga.trim() !== '') {
      equipos = equipos.filter(e => e.nombreLigaNoEspacio === this.filtroLiga);
    }
    return equipos;
  }

  get equiposPaginados() {
    const start = (this.page - 1) * this.pageSize;
    return this.equiposFiltrados.slice(start, start + this.pageSize);
  }

  totalPages() {
    return Math.ceil(this.equiposFiltrados.length / this.pageSize);
  }

  setPage(p: number) {
    if (p < 1 || p > this.totalPages()) return;
    this.page = p;
  }

  nextPage() {
    if (this.page < this.totalPages()) this.page++;
  }

  prevPage() {
    if (this.page > 1) this.page--;
  }

  onFiltroChange() {
    this.page = 1;
  }
}

