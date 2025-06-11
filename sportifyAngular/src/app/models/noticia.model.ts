export interface GetUserNoAsociacionesDto {
  username: string;
  // ...otros campos si los necesitas
}

export interface GetNombreEquipoDto {
  nombreEquipo: string;
  // ...otros campos si los necesitas
}

export interface GetNombreLiga {
  nombreLiga: string;
  // ...otros campos si los necesitas
}

export interface GetNombreDeporteDto {
  nombreDeporte: string;
  // ...otros campos si los necesitas
}

export interface Noticia {
  titular: string;
  cuerpo: string;
  multimedia: string[] | null;
  fechaCreacion: string;
  slug: string;
  usuario: GetUserNoAsociacionesDto;
  equipo: GetNombreEquipoDto | null;
  liga: GetNombreLiga | null;
  deporte: GetNombreDeporteDto | null;
}
