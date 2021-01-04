
/* 
Este servicio captura las peticiones http antes de ser enviadas y les agrega el bearer con el token, para 
que el back-end las reconozca como peticiones de un usuario que hizo su login. Aca se inyecta el
LoginService a traves del constructor, para asi obtener el token. Se clona el request ya que no se
puede alterar, y se le pone un bearer con el token. Este service debe ser registrado en el app module,
en el arreglo providers. Asi todas las peticiones http de la aplicacion pasan por este filtro.
*/

import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import { ConfiguracionesProyectoService } from '../Configuraciones/configuraciones-proyecto.service';


@Injectable()
export class AuthInterceptorService implements HttpInterceptor
{

  constructor(private config: ConfiguracionesProyectoService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> 
  {
    if (localStorage.getItem(this.config.nombreToken) != null)    // Si el usuario ya inicio sesion, valida que el token no este expirado
    {
      var exp = localStorage.getItem(this.config.nombreTiempoExpiracionToken);   // localStorage.getItem("expiracion_token_proyecto_composicion_musical")
  
      if (new Date().getTime() >= parseInt(exp))   // Si ya expiró el token
      {
        alert("Se termino el tiempo limite de uso del usuario");
        localStorage.removeItem(this.config.nombreToken);
        localStorage.removeItem(this.config.nombreTiempoExpiracionToken);
        this.router.navigate([this.config.rutaLogin]);   // this.router.navigate(["/Login"]); 
        window.location.reload();
      } 
    } 

    var token = localStorage.getItem(this.config.nombreToken);

    if (token != null)   // Si es cualquier otra ruta diferente del login
    {
      req = req.clone({
        setHeaders: { Authorization: "Bearer " + token }
      });
    }

    return next.handle(req).pipe( tap(() => {}, (err: any) => 
    {
      console.log("Se encontro un error");
      /*
      if (err instanceof HttpErrorResponse) {
        if (err.status !== 401) {
         return;
        }
        console.log("Se encontro un 401");
      }
      */

    }));
    
    //return next.handle(req);  // Se envia el nuevo request que ha sido clonado y se le agrego un header

  }
}




