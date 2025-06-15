export interface UserRegister {
  username: string;
  password: string;
  verifyPassword: string;
  email: string;
  verifyEmail: string;
  fechaNacimiento: string;
  nombre: string;
  profileImage?: File | null;
}
