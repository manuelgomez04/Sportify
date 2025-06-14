import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DeportesService } from '../../services/deportes.service';
import { LigasService } from '../../services/ligas.service';
import { EquiposService } from '../../services/equipos.service';

@Component({
  selector: 'app-nueva-noticia',
  templateUrl: './nueva-noticia.component.html',
  styleUrls: ['./nueva-noticia.component.css']
})
export class NuevaNoticiaComponent implements OnInit {
  noticiaForm: FormGroup;
  deportes: any[] = [];
  ligas: any[] = [];
  equipos: any[] = [];
  archivos: File[] = [];
  isDragOver = false;
  loading = false;
  error: string | null = null;
  success = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private deportesService: DeportesService,
    private ligasService: LigasService,
    private equiposService: EquiposService
  ) {
    this.noticiaForm = this.fb.group({
      titular: ['', Validators.required],
      cuerpo: ['', Validators.required],
      deporte: [''],
      liga: [''],
      equipo: ['']
    });
  }

  ngOnInit() {
    this.deportesService.getDeportes(0, 100).subscribe(resp => {
      this.deportes = resp.content || [];
    });
  }

  onDeporteChange() {
    const selectedNombreNoEspacio = this.noticiaForm.value.deporte;
    this.ligas = [];
    this.equipos = [];
    this.noticiaForm.patchValue({ liga: '', equipo: '' });
    if (selectedNombreNoEspacio) {
      this.ligasService.getLigasPorDeporte(selectedNombreNoEspacio, 0, 100).subscribe(resp => {
        this.ligas = resp.content || [];
      });
    }
  }

  onLigaChange() {
    const nombreLiga = this.noticiaForm.get('liga')?.value;
    this.equipos = [];
    this.noticiaForm.patchValue({ equipo: '' });
    if (nombreLiga) {
      this.equiposService.getEquiposPorLiga(nombreLiga, 0, 100).subscribe(resp => {
        this.equipos = resp.content || [];
      });
    }
  }

  onFilesSelected(event: any) {
    const files = Array.from(event.target.files) as File[];
    this.archivos = this.archivos.concat(files);
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;
    if (event.dataTransfer && event.dataTransfer.files) {
      const files = Array.from(event.dataTransfer.files) as File[];
      this.archivos = this.archivos.concat(files);
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;
  }

  eliminarArchivo(index: number) {
    this.archivos.splice(index, 1);
  }

  async onSubmit() {
    const value = this.noticiaForm.value;
    if (
      this.noticiaForm.invalid ||
      !value.deporte ||
      !value.liga ||
      !value.equipo
    ) {
      this.error = 'Completa todos los campos obligatorios';
      return;
    }
    this.loading = true;
    this.error = null;
    this.success = false;

    try {
      const formData = new FormData();
      const noticiaDto = {
        titular: value.titular,
        cuerpo: value.cuerpo,
        nombreDeporte: value.deporte,
        nombreLiga: value.liga,
        nombreEquipo: value.equipo
      };
      formData.append('createNoticiaRequest', new Blob([JSON.stringify(noticiaDto)], { type: 'application/json' }));
      this.archivos.forEach(file => formData.append('files', file));

      // Puedes mover esto a un servicio si lo prefieres
      const noticiaResp: any = await fetch('/noticias', {
        method: 'POST',
        body: formData
      }).then(r => r.json());
      this.success = true;
      this.noticiaForm.reset();
      this.archivos = [];
      setTimeout(() => this.router.navigate(['/noticias']), 1500);
    } catch (err: any) {
      this.error = 'Error al publicar la noticia';
    } finally {
      this.loading = false;
    }
  }
}

