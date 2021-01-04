
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FuncionesGlobalesService 
{

  constructor() { }

  public mostrarLoadingSpinner() 
  {
    document.querySelector("#loading-spinner")["style"].display = "block";
  }

  public ocultarLoadingSpinner() 
  {
    document.querySelector("#loading-spinner")["style"].display = "none";
  }

  public construirPaginacion(datos:any)
  {
    /*
    datos = {
      "querySelector": "#div_numeros_paginacion", 
      "contarDesdeCero": false,
      "paginaActual": this.paginaActual,
      "totalRegistros": totalRegistros,
      "totalPaginas": this.totalPaginas,
      "registrosPorPagina": registrosPorPagina,
      "pasoAtras": () => { this.paginaActual --; this.obtenerListaPrincipal() },  
      "pasoAdelante": () => { this.paginaActual ++; this.obtenerListaPrincipal() },
      "seleccionarPagina": () => { this.paginaActual = 2;  this.obtenerListaPrincipal() }
    }
    */

    var paginaActual = datos.paginaActual 
    var totalRegistros = datos.totalRegistros
    var totalPaginas = datos.totalPaginas
    var registrosPorPagina = datos.registrosPorPagina
    var contarDesdeCero = (datos.contarDesdeCero != null) ? datos.contarDesdeCero : true;

    var contenedor = document.querySelector(datos.querySelector);
    contenedor.innerHTML = "";

    const crearBotonPaginacion = (datos:any = {}) => 
    {
      var enlace = document.createElement("a"); 
      enlace.setAttribute("href","javascript:void(0)");
      enlace.setAttribute("class", "btn btn-default");
      enlace.innerText = datos.texto;

      if (datos.numeroPagina != null) enlace.setAttribute("numeroPagina", datos.numeroPagina);

      enlace.setAttribute("style", datos.estilos != null ? datos.estilos : "");
      enlace.addEventListener("click", datos.onclick);
      contenedor.appendChild(enlace);
    }

    if(totalPaginas > 1  &&  totalRegistros >= registrosPorPagina )
    {
      if(paginaActual != (contarDesdeCero ? 0 : 1))    // Ejemplo original java
      {  
        crearBotonPaginacion({ 
          "texto": "<", 
          //"onclick": () => {  paginaActual --; this.obtenerListaPrincipal() } 
          "onclick": datos.pasoAtras 
        });
      }

      var maximo_paginas_mostrado = 10; // Total de paginas desplegadas. Ej: paginas del 1 al 10,   del 10 al 19,   del 20 al 29 
      var inicio_paginacion = 0;        // Primer numero de la paginación
      var fin_paginacion = 0;           // Ultimo numero de la paginación

      if(totalPaginas <= maximo_paginas_mostrado) { // Si el numero total de paginas es menor o igual a 10	
        inicio_paginacion = contarDesdeCero ? 0 : 1;  
        fin_paginacion = totalPaginas;
      }
      else
      {
        if(paginaActual >  totalPaginas - maximo_paginas_mostrado)  // Esto impide que el numero de la pagina sobrepase el total de paginas
        {  
          if (contarDesdeCero) 
            inicio_paginacion = totalPaginas - maximo_paginas_mostrado;  
          else 
            inicio_paginacion = (totalPaginas - maximo_paginas_mostrado) + 1; 

          fin_paginacion = totalPaginas;
        } 
        else
        {
          if (paginaActual < maximo_paginas_mostrado) 
          {
            inicio_paginacion = contarDesdeCero ? 0 : 1;    
            fin_paginacion = maximo_paginas_mostrado;      
          }
          if(paginaActual >= maximo_paginas_mostrado) // Si la pagina actual es mayor o igual a diez  
          {                              
            var finLoop = 0;

            if(totalPaginas % maximo_paginas_mostrado == 0)   // Si la cantidad de resultados es divisible por el numero de paginas
              finLoop = Math.ceil((totalPaginas/maximo_paginas_mostrado)); 
            else
              finLoop = Math.ceil((totalPaginas/maximo_paginas_mostrado) + 1); 

            for(let i = 1; i < finLoop;  i++)  // Determinar donde empieza y termina la paginacion
            {
              if(paginaActual < maximo_paginas_mostrado * i) 
              {
                inicio_paginacion = maximo_paginas_mostrado * (i - 1);
                fin_paginacion = (maximo_paginas_mostrado * i) - 1; 
                break;  // Es importante parar aquí el looping del for, sino aparecerá solo el ultimo grupo de paginas
              }
            }
          }
        }
      }

      //for(let i=0; i<=totalPaginas; i++)  // DE ESTA FORMA SE IMPRIMIRÍA UNA PAGINACIÓN CON EL TOTAL DE LAS PAGINAS
      
      for(let i = inicio_paginacion; i <= fin_paginacion; i++)  // LA PAGINACIÓN QUEDA ORDENADA EN GRUPOS DE 10 PAGINAS
      {
        crearBotonPaginacion({ 
          "texto": contarDesdeCero ? (i + 1) : i,    
          "numeroPagina": i,
          "estilos": (paginaActual == i) ? 
            "background-color:#d9d0c5; border-radius: 25px;" : 
            null,
          //"onclick": (paginaActual != i) ? () => { paginaActual = i;  this.obtenerListaPrincipal() } : null
          "onclick": (paginaActual != i) ? datos.seleccionarPagina : null
        });
      }	

      if(paginaActual != totalPaginas)  // Si pagina es diferente de totalPaginas 
      {    
        crearBotonPaginacion({ 
          "texto": ">", 
          //"onclick": () => {  paginaActual ++; this.obtenerListaPrincipal() } 
          "onclick": datos.pasoAdelante 
        });
      }
    }  
  }
  
}
