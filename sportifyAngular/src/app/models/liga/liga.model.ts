export interface Liga {
  id: string;
  nombre: string;
  descripcion: string;
  nombreDeporte: string;
  nombreSinEspacio: string;
  imagen: string | null;
  deporteNoEspacio: string;
  deporteNombre: string;
}

export interface LigaPage {
  content: Liga[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
