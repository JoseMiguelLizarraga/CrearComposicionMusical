import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { ConfiguracionesProyectoService } from '../Configuraciones/configuraciones-proyecto.service';
//import { LoginService } from '../Login/login.service';

/* La interfaz CanActivate es para que se ejecute justo antes de que entremos al componente y podemos 
determinar si tiene permiso o no para entrar. Esta interfaz exige que implementemos el metodo 
canActivate 
https://www.youtube.com/watch?v=5caOatBzArA&list=PL0kIvpOlieSMxCFeJvjRV8GlLJ0VNx2LI&index=46
min 16
*/

@Injectable()
export class AuthGuardService implements CanActivate 
{

  constructor(private config: ConfiguracionesProyectoService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean 
  {
    if (this.config.nombreToken != null)  
    {
      return true;  // Si el usuario tiene permiso, puede entrar al componente que desea entrar
    } 
    else 
    {
      //this.router.navigate(["/login"]);  
      this.router.navigate([this.config.rutaLogin]);  // Lo redirige al sector del login
      return false;
    }
  }

}