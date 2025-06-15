export interface UsuarioAdmin {
  username: string;
  password: string;
  email: string; // <-- en realidad es el nombre
  fechaNacimiento: string;
  equiposSeguidos: EquipoSeguidoAdmin[];
  deportesSeguidos: DeporteSeguidoAdmin[];
  ligasSeguidas: LigaSeguidaAdmin[];
  nombre: string; // <-- en realidad es el email
  profileImage: string | null;
}

export interface EquipoSeguidoAdmin {
  id: string;
  nombre: string;
  nombreNoEspacio: string;
  nombreLiga: string;
  ciudad: string;
  pais: string;
  escudo: string;
  fechaCreacion: string;
  nombreLigaNoEspacio: string;
}

export interface DeporteSeguidoAdmin {
  id: string;
  nombre: string;
  descripcion: string;
  imagen: string | null;
  nombreNoEspacio: string;
}

export interface LigaSeguidaAdmin {
  id: string;
  nombre: string;
  descripcion: string;
  nombreDeporte: string;
  nombreSinEspacio: string;
  imagen: string | null;
  deporteNoEspacio: string;
  deporteNombre: string;
}
