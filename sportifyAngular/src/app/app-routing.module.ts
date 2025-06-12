import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NoticiasComponent } from './components/noticias/noticias.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { VerifyAccountComponent } from './components/verify-account/verify-account.component';
import { MeComponent } from './components/me/me.component';
import { EditarCuentaComponent } from './components/editar-cuenta/editar-cuenta.component';
import { LigasSeguidasComponent } from './components/ligas-seguidas/ligas-seguidas.component';
import { SeguidosComponent } from './components/seguidos/seguidos.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'noticias', component: NoticiasComponent },
  { path: 'home', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify-account', component: VerifyAccountComponent },
  { path: 'me', component: MeComponent },
  { path: 'editar-cuenta', component: EditarCuentaComponent },
  { path: 'ligas-seguidas', component: LigasSeguidasComponent },
  { path: 'seguidos', component: SeguidosComponent },

  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }