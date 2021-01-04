import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfiguracionesProyectoService } from './Configuraciones/configuraciones-proyecto.service';
import { finalize } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { FuncionesGlobalesService } from './Servicios/funciones-globales.service';

// ESTE ES EL COMPONENTE USADO POR DEFECTO AL ABRIR LA APLICACION Y ES DECLARADO DENTRO DE src/app/app.module.ts
// app-root  es un identificador para llamar a este componente desde la vista html (Sera una etiqueta html)

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent 
{
  private url = ""; 
  public autorizadoEntrar = false

  constructor(private config: ConfiguracionesProyectoService, private funciones: FuncionesGlobalesService, private http:HttpClient, private router:Router) 
  {
    this.url = config.rutaWebApi;
  }

  alCargarComponente()
  {
    //console.log("autorizadoEntrar: " + this.autorizadoEntrar);
  }

  loguearse(jsonDatosForm:any) 
  {
    if (jsonDatosForm.username == "" || jsonDatosForm.password == "")
    {
      alert("Usuario o clave incompletos");
    }
    else
    {
      this.funciones.mostrarLoadingSpinner();

      this.http.post(this.url + "/login", JSON.stringify(jsonDatosForm), {headers: {"Content-Type": "application/json"}})
      .pipe( finalize(() => { this.funciones.ocultarLoadingSpinner() }))
      .subscribe(
        (datos:any) => {

          this.autorizadoEntrar = true; 
          localStorage.setItem(this.config.nombreToken, datos.token);
          localStorage.setItem(this.config.nombreTiempoExpiracionToken, datos.expiration);   // Expiracion del token
      
          //this.router.navigate(["/composicionMusical/crearEditar"]);
          this.router.navigate([this.config.rutaLoginRealizado]);
        },
        (excepcion) => {
          this.autorizadoEntrar = false; 
          alert(excepcion.error.message);
        }
      );
    }
  }

  SalirSistema(evento)    
  {
    evento.preventDefault();

    localStorage.removeItem(this.config.nombreToken);
    localStorage.removeItem(this.config.nombreTiempoExpiracionToken);
    this.router.navigate([this.config.rutaLogoutRealizado]); 
    this.autorizadoEntrar = false; 
  }

}
