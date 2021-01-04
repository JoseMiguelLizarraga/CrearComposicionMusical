
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ExcepcionesService 
{

  constructor() { }

  tratarExcepcion(excepcion)
  {
    console.log(excepcion);
    
    if (excepcion.status != null) 
    {
      if (excepcion.status == 401 && excepcion.error != null && excepcion.error.message != null)  // Si no esta autorizado y recibio un mensaje
      {
        alert(excepcion.error.message);
      }
      else if (excepcion.status == 0) 
      {
        console.log("No se encontro la ruta " + excepcion.url);
      }
      else 
      {
        //console.log(excepcion);
      }
    }
    
  }


}

