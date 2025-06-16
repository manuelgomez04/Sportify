export interface NoticiaDetalle {
  id: string;
  titular: string;
  cuerpo: string;
  multimedia: string[];
  usuario: UsuarioDetalle;
  fechaCreacion: string;
  equipo: EquipoDetalle;
  deporte: DeporteDetalle;
  liga: LigaDetalle;
  slug: string;
  comentarios: ComentariosDetallePage;
  likesCount: number;
  comentariosCount: number;
}

export interface UsuarioDetalle {
  username: string;
  fotoPerfil: string | null;
}

export interface EquipoDetalle {
  nombreEquipo: string;
  escudo: string | null;
}

export interface DeporteDetalle {
  nombreDeporte: string;
  imagen: string | null;
}

export interface LigaDetalle {
  nombreLiga: string;
  imagen: string | null;
}

export interface ComentarioDetalle {
  usuario: UsuarioDetalle;
  noticia: { titular: string };
  comentario: string;
  titular: string;
  fecha: string;
}

export interface ComentariosDetallePage {
  content: ComentarioDetalle[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
