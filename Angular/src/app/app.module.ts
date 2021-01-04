import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';  // Esto lo puse yo

// Esto fue añadido
//import { FormsModule } from '@angular/forms';     // Debe agregarse al arreglo imports
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import { AuthGuardService } from './Servicios/auth-guard.service';  // Debe agregarse al arreglo providers
import { AuthInterceptorService } from './Servicios/auth-interceptor.service';
import { CrearEditarComposicionMusicalComponent } from './Componentes/ComposicionMusical/crear-editar-composicion-musical/crear-editar-composicion-musical.component';
import { ListarComposicionMusicalComponent } from './Componentes/ComposicionMusical/listar-composicion-musical/listar-composicion-musical.component';
import { LoginComponent } from './Componentes/login/login.component';
//import { VentasModule } from './ventas/ventas.module';

/* 
Dentro del arreglo providers:
La linea que dice HTTP_INTERCEPTORS es para tomar todas las peticiones http de la aplicacion y agregarles
un bearer dentro del header, para ser reconocidas por el back-end como de un usuario que hizo su login.
*/

// Dentro del arreglo   imports    agregue el modulo  ventas   para que pueda ser utilizado por el proyecto

@NgModule({
  declarations: [
    AppComponent,
    CrearEditarComposicionMusicalComponent,
    ListarComposicionMusicalComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule
    //VentasModule
  ],
  providers: [
    AppComponent,  // Esto es para usar los metodos del componente principal desde otros componentes
    AuthGuardService, 
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
