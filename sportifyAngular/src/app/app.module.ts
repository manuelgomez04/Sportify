import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NoticiasComponent } from './components/noticias/noticias.component';
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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NoticiasComponent,
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
    NuevaNoticiaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthRedirectInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
