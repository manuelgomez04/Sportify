export interface Equipo {
  id: string;
  nombre: string;
  nombreNoEspacio: string;
  nombreLiga: string;
  ciudad: string;
  pais: string;
  escudo: string | null;
  fechaCreacion: string;
  nombreLigaNoEspacio: string;
}

export interface EquipoPage {
  content: Equipo[];
  totalPages: number;
  number: number;
  size: number;
}
