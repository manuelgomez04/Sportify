import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { VerifyAccountComponent } from './components/verify-account/verify-account.component';
import { MeComponent } from './components/me/me.component';
import { EditarCuentaComponent } from './components/editar-cuenta/editar-cuenta.component';
import { LigasSeguidasComponent } from './components/ligas-seguidas/ligas-seguidas.component';
import { SeguidosComponent } from './components/seguidos/seguidos.component';
import { DeportesSeguidosComponent } from './components/deportes-seguidos/deportes-seguidos.component';
import { EquiposSeguidosComponent } from './components/equipos-seguidos/equipos-seguidos.component';
import { NoticiasFavoritasComponent } from './components/noticias-favoritas/noticias-favoritas.component';
import { MisComentariosComponent } from './components/mis-comentarios/mis-comentarios.component';
import { NuevaNoticiaComponent } from './components/nueva-noticia/nueva-noticia.component';
import { WriterAdminGuard } from './guards/writer-admin.guard';
import { AuthGuard } from './guards/auth.guard';
import { MisNoticiasComponent } from './components/mis-noticias/mis-noticias.component';
import { DetalleNoticiaComponent } from './components/detalle-noticia/detalle-noticia.component';
import { DeportesComponent } from './components/deportes/deportes.component';
import { LigasComponent } from './components/ligas/ligas.component';
import { EquiposComponent } from './components/equipos/equipos.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminDeportesComponent } from './components/admin-deportes/admin-deportes.component';
import { AdminGuard } from './guards/admin.guard';
import { AdminLigasComponent } from './components/admin-ligas/admin-ligas.component';
import { AdminEquiposComponent } from './components/admin-equipos/admin-equipos.component';
import { AdminUsuariosComponent } from './components/admin-usuarios/admin-usuarios.component';



const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify-account', component: VerifyAccountComponent },
  {
    path: 'me',
    component: MeComponent,
    canActivate: [AuthGuard]
  },
  { path: 'editar-cuenta', component: EditarCuentaComponent, canActivate: [AuthGuard] },
  { path: 'ligas-seguidas', component: LigasSeguidasComponent },
  { path: 'seguidos', component: SeguidosComponent },
  { path: 'deportes-seguidos', component: DeportesSeguidosComponent },
  { path: 'equipos-seguidos', component: EquiposSeguidosComponent },
  { path: 'noticias-favoritas', component: NoticiasFavoritasComponent },
  { path: 'mis-comentarios', component: MisComentariosComponent },
  {
    path: 'nueva-noticia',
    component: NuevaNoticiaComponent,
    canActivate: [WriterAdminGuard] 
  },
  { path: 'mis-noticias', component: MisNoticiasComponent, canActivate: [WriterAdminGuard] },
  { path: 'noticias/:slug', component: DetalleNoticiaComponent },
  { path: 'deportes', component: DeportesComponent },
  { path: 'ligas', component: LigasComponent },
  { path: 'equipos', component: EquiposComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AdminGuard] },
  { path: 'admin/deportes', component: AdminDeportesComponent, canActivate: [AdminGuard] },
  { path: 'admin/ligas', component: AdminLigasComponent, canActivate: [AdminGuard] },
  { path: 'admin/equipos', component: AdminEquiposComponent, canActivate: [AdminGuard] },
  { path: 'admin/usuarios', component: AdminUsuariosComponent, canActivate: [AdminGuard] },

  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }