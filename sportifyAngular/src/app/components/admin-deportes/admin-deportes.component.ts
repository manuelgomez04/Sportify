import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DeportesService } from '../../services/deportes.service';

@Component({
  selector: 'app-admin-deportes',
  templateUrl: './admin-deportes.component.html',
  styleUrls: ['./admin-deportes.component.css']
})
export class AdminDeportesComponent implements OnInit {
  deportes: any[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  deporteAEliminar: any = null;
  showDeleteModal = false;
  deleteLoading: string | null = null;
  deleteError: string | null = null;
  showAddModal = false;
  addDeporteForm: FormGroup;
  addLoading = false;
  addError: string | null = null;
  addSuccess = false;
  imagenFile: File | null = null;
  MAX_FILE_SIZE_MB = 5; // Tamaño máximo permitido en MB

  constructor(
    private fb: FormBuilder,
    private deportesService: DeportesService // Asegúrate de tener este servicio
  ) {
    this.addDeporteForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      imagen: [null, Validators.required]
    });
  }

  ngOnInit() {
    this.cargarDeportes(0);
  }

  cargarDeportes(page: number) {
    this.deportesService.getDeportes(page, this.size).subscribe(resp => {
      this.deportes = resp.content || [];
      this.page = resp.number;
      this.totalPages = resp.totalPages;
    });
  }

  abrirEliminar(deporte: any) {
    this.deporteAEliminar = deporte;
    this.deleteError = null;
    this.showDeleteModal = true;
  }

  cerrarEliminar() {
    this.deporteAEliminar = null;
    this.deleteError = null;
    this.showDeleteModal = false;
  }

  eliminarDeporteConfirmado() {
    if (!this.deporteAEliminar) return;
    this.deleteLoading = this.deporteAEliminar.nombreNoEspacio;
    this.deleteError = null;
    this.deportesService.eliminarDeporte(this.deporteAEliminar.nombreNoEspacio).subscribe({
      next: () => {
        this.cerrarEliminar();
        this.cargarDeportes(this.page);
      },
      error: () => {
        this.deleteError = 'Error al eliminar el deporte';
      },
      complete: () => {
        this.deleteLoading = null;
      }
    });
  }

  eliminarDeporte(nombreNoEspacio: string) {
    const deporte = this.deportes.find(d => d.nombreNoEspacio === nombreNoEspacio);
    if (deporte) {
      this.abrirEliminar(deporte);
    }
  }

  abrirAddDeporte() {
    this.addDeporteForm.reset();
    this.imagenFile = null;
    this.addError = null;
    this.addSuccess = false;
    this.showAddModal = true;
  }

  cerrarAddDeporte() {
    this.showAddModal = false;
    this.addError = null;
    this.addSuccess = false;
    this.imagenFile = null;
  }

  onImagenSelected(event: any) {
    const file = event.target.files && event.target.files[0];
    if (file) {
      if (file.size > this.MAX_FILE_SIZE_MB * 1024 * 1024) {
        this.addError = 'Archivo demasiado pesado';
        this.imagenFile = null;
        this.addDeporteForm.get('imagen')?.setErrors({ fileSize: true });
        this.addLoading = false;
      } else {
        this.imagenFile = file;
        this.addDeporteForm.patchValue({ imagen: file });
        this.addDeporteForm.get('imagen')?.setErrors(null);
        this.addError = null;
        this.addLoading = false;
      }
    } else {
      this.imagenFile = null;
      this.addDeporteForm.patchValue({ imagen: null });
      this.addDeporteForm.get('imagen')?.setErrors({ required: true });
      this.addError = null;
      this.addLoading = false;
    }
  }

  crearDeporte() {
    if (this.addDeporteForm.invalid || !this.imagenFile) {
      if (this.addDeporteForm.get('imagen')?.hasError('fileSize')) {
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
      nombre: this.addDeporteForm.value.nombre,
      descripcion: this.addDeporteForm.value.descripcion
    };
    formData.append('createDeporteRequest', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
    formData.append('imagen', this.imagenFile);

    this.deportesService.crearDeporte(formData).subscribe({
      next: () => {
        this.cerrarAddDeporte();
        this.cargarDeportes(this.page);
      },
      error: (err) => {
        if (err.status === 413) {
          this.addError = 'Archivo demasiado pesado';
        } else {
          this.addError = 'Error al crear el deporte';
        }
        this.addLoading = false;
      },
      complete: () => {
        this.addLoading = false;
      }
    });
  }
}
