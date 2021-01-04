import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfiguracionesProyectoService 
{
  rutaWebApi = "http://localhost:8080/api"; 

  nombreToken = "token_proyecto_composicion_musical";
  nombreTiempoExpiracionToken = "expiracion_token_proyecto_composicion_musical";  

  rutaLogin = "/login";
  rutaLoginRealizado = "/composicionMusical/crearEditar";
  rutaLogoutRealizado = "/login";

  constructor() { }
  
}
