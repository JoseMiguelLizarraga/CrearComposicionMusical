import { Injectable } from '@angular/core';

declare const configuracionesProyecto: any;   // Esto trae la funcion configuracionesProyecto que esta en assets

@Injectable({
  providedIn: 'root'
})
export class ConfiguracionesProyectoService 
{
  rutaWebApi = ""; 

  nombreIdUsuarioAutenticado = "id_usuario_autenticado_proyecto_composicion_musical";
  nombreToken = "token_proyecto_composicion_musical";
  nombreTiempoExpiracionToken = "expiracion_token_proyecto_composicion_musical";  

  rutaLogin = "/login";
  rutaLoginRealizado = "/composicionMusical/crearEditar";
  rutaLogoutRealizado = "/login";

  constructor() { 
    this.rutaWebApi = configuracionesProyecto.rutaWebApi;  // Asi la url se puede cambiar al estar publicado
  }
  
}
