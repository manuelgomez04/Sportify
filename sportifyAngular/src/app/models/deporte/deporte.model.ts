export interface Deporte {
  id: string;
  nombre: string;
  descripcion: string;
  imagen: string | null;
  nombreNoEspacio: string;
}

export interface DeportePage {
  content: Deporte[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
