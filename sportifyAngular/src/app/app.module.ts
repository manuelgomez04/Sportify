import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { NavbarComponent } from './components/navbar/navbar.component';
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
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthRedirectInterceptor } from './interceptors/auth-redirect.interceptor';
import { MisNoticiasComponent } from './components/mis-noticias/mis-noticias.component';
import { DetalleNoticiaComponent } from './components/detalle-noticia/detalle-noticia.component';
import { CommonModule, HashLocationStrategy, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { environment } from '../environments/environment.development';
import { DeportesComponent } from './components/deportes/deportes.component';
import { LigasComponent } from './components/ligas/ligas.component';
import { EquiposComponent } from './components/equipos/equipos.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminDeportesComponent } from './components/admin-deportes/admin-deportes.component';
import { LigaDeporteFilterPipe } from './pipes/liga-deporte-filter.pipe';
import { AdminLigasComponent } from './components/admin-ligas/admin-ligas.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSortModule } from '@angular/material/sort';
import { MatSelectModule } from '@angular/material/select';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    NavbarComponent,
    RegisterComponent,
    VerifyAccountComponent,
    MeComponent,
    EditarCuentaComponent,
    LigasSeguidasComponent,
    SeguidosComponent,
    DeportesSeguidosComponent,
    EquiposSeguidosComponent,
    NoticiasFavoritasComponent,
    MisComentariosComponent,
    NuevaNoticiaComponent,
    MisNoticiasComponent,
    DetalleNoticiaComponent,
    DeportesComponent,
    LigasComponent,
    EquiposComponent,
    AdminComponent,
    AdminDeportesComponent,
    LigaDeporteFilterPipe,
    AdminLigasComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSortModule,
    CommonModule,
    MatSelectModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthRedirectInterceptor,
      multi: true
    },
    { provide: LocationStrategy, useClass: environment.useHash ? HashLocationStrategy : PathLocationStrategy },
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
