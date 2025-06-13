export interface ComentarioUsuario {
  usuario: { username: string , fotoPerfil: string};
  noticia: { titular: string, slug: string };
  comentario: string;
  titular: string;
  fecha: string;
  
}

export interface ComentariosUsuarioPage {
  content: ComentarioUsuario[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
