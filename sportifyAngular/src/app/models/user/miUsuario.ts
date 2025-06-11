export interface miUsuario {
  username: string
  password: string
  email: string
  fechaNacimiento: string
  equiposSeguidos: EquiposSeguido[]
  deportesSeguidos: DeportesSeguido[]
  ligasSeguidas: LigasSeguida[]
  nombre: string
}

export interface EquiposSeguido {
  id: string
  nombre: string
  nombreNoEspacio: string
  nombreLiga: string
  ciudad: string
  pais: string
  escudo: string
  fechaCreacion: string
}

export interface DeportesSeguido {
  id: string
  nombre: string
  descripcion: string
}

export interface LigasSeguida {
  id: string
  nombre: string
  descripcion: string
  nombreDeporte: string
  nombreSinEspacio: string
}
