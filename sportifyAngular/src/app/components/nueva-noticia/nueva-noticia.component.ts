import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

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
    private http: HttpClient,
    private router: Router
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
    this.http.get<any[]>('/deporte').subscribe(data => this.deportes = data);
  }

  onDeporteChange() {
    // Busca el objeto deporte seleccionado para obtener su nombreNoEspacio
    const selectedNombreNoEspacio = this.deportes.find(
      d => d.nombreNoEspacio === this.noticiaForm.value.deporte
    )?.nombreNoEspacio || this.noticiaForm.value.deporte;

    this.ligas = [];
    this.equipos = [];
    this.noticiaForm.patchValue({ liga: '', equipo: '' });
    if (selectedNombreNoEspacio) {
      this.http.get<any[]>(`/liga/por-deporte/${selectedNombreNoEspacio}`).subscribe(data => this.ligas = data);
    }
  }

  onLigaChange() {
    const nombreLiga = this.noticiaForm.get('liga')?.value;
    this.equipos = [];
    this.noticiaForm.patchValue({ equipo: '' });
    if (nombreLiga) {
      this.http.get<any[]>(`/equipo/por-liga/${nombreLiga}`).subscribe(data => this.equipos = data);
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
    // Validación manual de selects dependientes
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

      const noticiaResp: any = await this.http.post('/noticias', formData).toPromise();
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
