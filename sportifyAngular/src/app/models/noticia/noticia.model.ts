export interface Noticia {
  id: string
  titular: string
  cuerpo: string
  multimedia: string[]
  usuario: Usuario
  fechaCreacion: string
  equipo: Equipo
  deporte: Deporte
  liga: Liga
  slug: string
}

export interface Usuario {
  username: string
}

export interface Equipo {
  nombreEquipo: string
}

export interface Deporte {
  nombreDeporte: string
}

export interface Liga {
  nombreLiga: string
}
