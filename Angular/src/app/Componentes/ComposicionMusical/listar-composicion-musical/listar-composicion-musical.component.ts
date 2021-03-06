import { Component, OnInit, AfterViewInit, ViewChild } from "@angular/core"; 
import { NgForm } from "@angular/forms"; 
import { HttpClient, HttpHeaders } from "@angular/common/http"; 
import { FuncionesGlobalesService } from 'src/app/Servicios/funciones-globales.service';
import { finalize } from 'rxjs/operators';
import { CabeceraComposicionMusical } from 'src/app/Modelo/CabeceraComposicionMusical';
import { Usuario } from 'src/app/Modelo/Usuario';
import { ConfiguracionesProyectoService } from 'src/app/Configuraciones/configuraciones-proyecto.service';
import { ExcepcionesService } from 'src/app/Servicios/excepciones.service';

@Component({
  selector: 'app-listar-composicion-musical',
  templateUrl: './listar-composicion-musical.component.html',
  styleUrls: ['./listar-composicion-musical.component.css']
})
export class ListarComposicionMusicalComponent implements OnInit 
{ 
	public operacion = "listar"; 
	public url = ""; 
	public listaCabeceraComposicionMusical = Array<CabeceraComposicionMusical>();                
	public cabeceraComposicionMusical:CabeceraComposicionMusical = new CabeceraComposicionMusical();      
	public listasBuscador = []; 
	//public headers = {headers: {"Content-Type": "application/json"}}; 

	public paginaActual = 0;    // Atributo opcional en caso de usar Paginación 
	public totalPaginas = 0;    // Atributo opcional en caso de usar Paginación 
	
	public arregloNotas = [];

	public paginacionListaDetalleComposicionMusical = {
		pagina: 1,
		registrosPorPagina: 4,
		totalPaginas: 0,  
		despliegue: []
	}

	constructor(private config: ConfiguracionesProyectoService, private funciones: FuncionesGlobalesService, private http:HttpClient, private excepciones: ExcepcionesService) 
	{  
		this.url = config.rutaWebApi;
		this.inicializarArregloNotas();
	} 

	ngOnInit() { 
		this.obtenerListaPrincipal(); 
		this.obtenerListasBuscador(); 
	} 

	paginarListaDetalleComposicionMusical()
	{
		var registrosPorPagina = this.paginacionListaDetalleComposicionMusical.registrosPorPagina;
        var pagina = Math.abs(this.paginacionListaDetalleComposicionMusical.pagina);
        pagina = pagina > 0 ? pagina - 1 : pagina;

		var arreglo = this.cabeceraComposicionMusical.listaDetalleComposicionMusical;
		this.paginacionListaDetalleComposicionMusical.totalPaginas = Math.ceil( arreglo.length / registrosPorPagina ); 

		this.paginacionListaDetalleComposicionMusical.despliegue = (arreglo.filter((value, n) => {
            return (n >= (pagina * registrosPorPagina)) && (n < ((pagina + 1) * registrosPorPagina))
		}));

		//================================================>>>> Agregado 
		
		this.funciones.construirPaginacion({
			"querySelector": "#div_numeros_paginacion", 
			"contarDesdeCero": false,
			"paginaActual": this.paginacionListaDetalleComposicionMusical.pagina,
			"totalRegistros": this.cabeceraComposicionMusical.listaDetalleComposicionMusical.length,
			"totalPaginas": this.paginacionListaDetalleComposicionMusical.totalPaginas,
			"registrosPorPagina": this.paginacionListaDetalleComposicionMusical.registrosPorPagina,
			"pasoAtras": () => { 
				this.paginacionListaDetalleComposicionMusical.pagina --; 
				this.paginarListaDetalleComposicionMusical()
			},  
			"pasoAdelante": () => { 
				this.paginacionListaDetalleComposicionMusical.pagina ++; 
				this.paginarListaDetalleComposicionMusical()
			},
			"seleccionarPagina": (e) => { 
				this.paginacionListaDetalleComposicionMusical.pagina = parseInt(e.target.getAttribute("numeroPagina"));
				this.paginarListaDetalleComposicionMusical()
			}
		});

		//================================================>>>> Agregado 
	}

	ngAfterViewInit() { } 

  marcarTeclaRecorrida(nota, detalleComposicionMusical)   // NUEVO
  {
    if ( Array.from(detalleComposicionMusical.listaSubDetalleComposicionMusical.filter(a=> a.nota == nota)).length > 0 ) {
      return true;
    }
    return false;
  }

  tocarTecla(evento, detalleComposicionMusical)
  {
    var jsonNotaMusical = this.arregloNotas.find(c=> c.nota == evento.target.id);
    //console.log(jsonNotaMusical.posicion);

    if (detalleComposicionMusical.listaSubDetalleComposicionMusical.find(c=> c.nota == evento.target.id))  // Si ya tiene la nota
    {   
      evento.target.classList.remove("tecla_pulsada");  // Remueve la clase tecla_pulsada

      detalleComposicionMusical.listaSubDetalleComposicionMusical.splice(detalleComposicionMusical.listaSubDetalleComposicionMusical 
      .findIndex(c=> c.nota == evento.target.id), 1);   

    } 
    else   // Si no tiene la nota, la agrega
    {
      evento.target.classList.add("tecla_pulsada");  // Agrega la clase tecla_pulsada
      
      detalleComposicionMusical.listaSubDetalleComposicionMusical.push({ 
        posicionTecla: jsonNotaMusical.posicion, 
        nota: jsonNotaMusical.nota, 
      }); 

      var audio = new Audio("../../../assets/Sonidos/" + jsonNotaMusical.sonido);
      audio.play();
    }

  }


  tocarAcorde(detalleComposicionMusical)
  {
    detalleComposicionMusical.listaSubDetalleComposicionMusical.forEach(c=> 
    {
      var jsonNotaMusical = this.arregloNotas.find(x=> x.posicion == c.posicionTecla);
      var audio = new Audio("../../../assets/Sonidos/" + jsonNotaMusical.sonido);
      audio.play();
    });

  }

 
  modificarTono(opcion)
  {
	const posicionMasBaja = this.arregloNotas.reduce((prev, current) => (prev.posicion < current.posicion) ? prev : current).posicion;   // Es 0
	const posicionMasAlta = this.arregloNotas.reduce((prev, current) => (prev.posicion > current.posicion) ? prev : current).posicion;   // Es 84

	for (let c of this.cabeceraComposicionMusical.listaDetalleComposicionMusical.map(x=> x.listaSubDetalleComposicionMusical.map(z=> z.posicionTecla))) 
	{
		if (opcion == "bajar" && c.includes(posicionMasBaja)) {
		  return alert("No es posible bajar más de tono porque se encuentra en el límite de las teclas del piano");
        }
        if (opcion == "subir" && c.includes(posicionMasAlta)) {
			return alert("No es posible subir más de tono porque se encuentra en el límite de las teclas del piano");
        }
	}

    this.cabeceraComposicionMusical.listaDetalleComposicionMusical.forEach(detalle=> 
    {
      // Remueve la clase   tecla_pulsada   de todas las teclas del piano recorrido
	  //document.querySelectorAll("ul#piano_" + detalle.id + " > li > .tecla").forEach(c=> { c.classList.remove("tecla_pulsada"); });

      detalle.listaSubDetalleComposicionMusical.forEach(subDetalle=> 
      {
        var jsonNotaMusical;

        if (opcion == "bajar") {
          jsonNotaMusical = this.arregloNotas.find(x=> x.posicion == subDetalle.posicionTecla - 1);
        }
        if (opcion == "subir") {
          jsonNotaMusical = this.arregloNotas.find(x=> x.posicion == subDetalle.posicionTecla + 1);
        }

        subDetalle.nota = jsonNotaMusical.nota;
        subDetalle.posicionTecla = jsonNotaMusical.posicion;
      });

    });
  }

  //==========================================================================================================>>>>>>>>

	obtenerConsultaBuscador() 
	{ 
		//var visibleId = document.getElementById("visibleId"); 
		var autorId = document.getElementById("autorId"); 
		var tituloId = document.getElementById("tituloId"); 
		var usuarioId = document.getElementById("usuarioId"); 

		var consulta = {}; 

		// if (visibleId != null && ! ["", null].includes(visibleId["value"])) 
		// 	consulta["visible"] = visibleId["value"]; 

		if (autorId != null && ! ["", null].includes(autorId["value"])) 
			consulta["autor"] = autorId["value"]; 

		if (tituloId != null && ! ["", null].includes(tituloId["value"])) 
			consulta["titulo"] = tituloId["value"]; 

		if (usuarioId != null && ! ["", null].includes(usuarioId["value"])) 
			consulta["usuario.id"] = usuarioId["value"];   // Foreign key  

		return consulta; 
	} 


	obtenerListaPrincipal()  // Obtener lista de CabeceraComposicionMusical de la Web Api 
	{ 
		var registrosPorPagina = 10; 
		var inicio = this.paginaActual * registrosPorPagina; 

		var consulta = this.obtenerConsultaBuscador(); 
		consulta["start"] = inicio;                 // Datos para paginacion 
		consulta["length"] = registrosPorPagina;    // Datos para paginacion 

		var parametros = Object.entries(consulta).map(c=> c.join("=")).join("&"); 
		this.funciones.mostrarLoadingSpinner();

		this.http.get<CabeceraComposicionMusical[]>(this.url + "/CabeceraComposicionMusical/llenarDataTable?" + parametros)
		.pipe( finalize(() => { this.funciones.ocultarLoadingSpinner() }))
		.subscribe( 
			data => { 
				this.listaCabeceraComposicionMusical = data["data"]; 
				this.totalPaginas = Math.ceil( parseInt(data["recordsTotal"]) / registrosPorPagina ) - 1; 
			}
		); 
	} 

	resetearPaginacion() 
	{ 
		this.paginaActual = 0; 
		this.totalPaginas = 0; 
	} 

	// En caso de que la entidad del mantenedor sea hija de otra entidad o tenga referencias cruzadas 
	obtenerListasBuscador()  // Obtener lista de CabeceraComposicionMusical de la Web Api 
	{ 
		this.funciones.mostrarLoadingSpinner();

		this.http.get<String[]>(this.url + "/CabeceraComposicionMusical/obtenerListasBuscador")
		.pipe( finalize(() => { this.funciones.ocultarLoadingSpinner() }))
		.subscribe( data => { 
			this.listasBuscador = data; 
		}); 
	} 


	editar(id)  // Obtener CabeceraComposicionMusical de la Web Api por su id 
	{ 
		this.funciones.mostrarLoadingSpinner();

		this.http.get<CabeceraComposicionMusical>(this.url + "/CabeceraComposicionMusical" + "/" + id)
		.pipe( finalize(() => { this.funciones.ocultarLoadingSpinner() }))
		.subscribe(
			(data) => 
			{ 
				this.operacion = "editar"; 
				this.cabeceraComposicionMusical = data; 

				this.cabeceraComposicionMusical.listaDetalleComposicionMusical.forEach(detalle=> 
				{
					//var texto = detalle["cadenaListaSubDetalles"];
					//texto = texto.split(/\\/).join("");                // Remueve los backslash para asi poder convertir la cadena en json
					//console.log( JSON.parse(texto) );
					detalle.listaSubDetalleComposicionMusical = JSON.parse( detalle["cadenaListaSubDetalles"].split(/\\/).join("") );

					detalle.cadenaListaSubDetalles = null;
				});

				this.cabeceraComposicionMusical.listaDetalleComposicionMusical.sort((a, b) => a.orden - b.orden);  // Ordena la lista de forma ascendente por su orden
				this.paginarListaDetalleComposicionMusical();                                                         // Realiza la paginacion de la lista de detalles

			}
		); 
	} 


	inicializarArregloNotas()
	{
		this.arregloNotas = [
		{ posicion: 0, nota: "DO_0", sonido: "sonidos_piano_extra/0C.mp3" },
		{ posicion: 1, nota: "DO_0_sostenido", sonido: "sonidos_piano_extra/0Cs.mp3" },
		{ posicion: 2, nota: "RE_0", sonido: "sonidos_piano_extra/0D.mp3" },
		{ posicion: 3, nota: "RE_0_sostenido", sonido: "sonidos_piano_extra/0Ds.mp3" },
		{ posicion: 4, nota: "MI_0", sonido: "sonidos_piano_extra/0E.mp3" },
		{ posicion: 5, nota: "FA_0", sonido: "sonidos_piano_extra/0F.mp3" },
		{ posicion: 6, nota: "FA_0_sostenido", sonido: "sonidos_piano_extra/0Fs.mp3" },
		{ posicion: 7, nota: "SOL_0", sonido: "sonidos_piano_extra/0G.mp3" },
		{ posicion: 8, nota: "SOL_0_sostenido", sonido: "sonidos_piano_extra/0Gs.mp3" },
		{ posicion: 9, nota: "LA_0", sonido: "sonidos_piano/220-A.mp3" },
		{ posicion: 10, nota: "LA_0_sostenido", sonido: "sonidos_piano_extra/0As.mp3" },
		{ posicion: 11, nota: "SI_0", sonido: "sonidos_piano/246-B.mp3" },
		{ posicion: 12, nota: "DO_1", sonido: "sonidos_piano/261-C.mp3" },
		{ posicion: 13, nota: "DO_1_sostenido", sonido: "sonidos_piano/277-C-sharp.mp3" },
		{ posicion: 14, nota: "RE_1", sonido: "sonidos_piano/293-D.mp3" },
		{ posicion: 15, nota: "RE_1_sostenido", sonido: "sonidos_piano/311-D-sharp.mp3" },
		{ posicion: 16, nota: "MI_1", sonido: "sonidos_piano/329-E.mp3" },
		{ posicion: 17, nota: "FA_1", sonido: "sonidos_piano/349-F.mp3" },
		{ posicion: 18, nota: "FA_1_sostenido", sonido: "sonidos_piano/369F-sharp.mp3" },
		{ posicion: 19, nota: "SOL_1", sonido: "sonidos_piano/391-G.mp3" },
		{ posicion: 20, nota: "SOL_1_sostenido", sonido: "sonidos_piano/415-G-sharp.mp3" },
		{ posicion: 21, nota: "LA_1", sonido: "sonidos_piano/440-A.mp3" },
		{ posicion: 22, nota: "LA_1_sostenido", sonido: "sonidos_piano/466-A-sharp.mp3" },
		{ posicion: 23, nota: "SI_1", sonido: "sonidos_piano/495-B.mp3" },
		{ posicion: 24, nota: "DO_2", sonido: "sonidos_piano/523-C.mp3" },
		{ posicion: 25, nota: "DO_2_sostenido", sonido: "sonidos_piano/545-C-sharp.mp3" },
		{ posicion: 26, nota: "RE_2", sonido: "sonidos_piano/587-D.mp3" },
		{ posicion: 27, nota: "RE_2_sostenido", sonido: "sonidos_piano/622-D-sharp.mp3" },
		{ posicion: 28, nota: "MI_2", sonido: "sonidos_piano/659-E.mp3" },
		{ posicion: 29, nota: "FA_2", sonido: "sonidos_piano/698-F.mp3" },
		{ posicion: 30, nota: "FA_2_sostenido", sonido: "sonidos_piano/698-F-sharp.mp3" },
		{ posicion: 31, nota: "SOL_2", sonido: "sonidos_piano/783-G.mp3" },
		{ posicion: 32, nota: "SOL_2_sostenido", sonido: "sonidos_piano/830-G-sharp.mp3" },
		{ posicion: 33, nota: "LA_2", sonido: "sonidos_piano/880-A.mp3" },
		{ posicion: 34, nota: "LA_2_sostenido", sonido: "sonidos_piano/932-A-sharp.mp3" },
		{ posicion: 35, nota: "SI_2", sonido: "sonidos_piano/987-B.mp3" },
		{ posicion: 36, nota: "DO_3", sonido: "sonidos_piano/1046-C.mp3" },
		{ posicion: 37, nota: "DO_3_sostenido", sonido: "sonidos_parche/DO_3_sostenido.mp3" },
		{ posicion: 38, nota: "RE_3", sonido: "sonidos_parche/RE_3.mp3" },
		{ posicion: 39, nota: "RE_3_sostenido", sonido: "sonidos_piano_extra/3Ds.mp3" },
		{ posicion: 40, nota: "MI_3", sonido: "sonidos_piano_extra/3E.mp3" },
		{ posicion: 41, nota: "FA_3", sonido: "sonidos_piano_extra/3F.mp3" },
		{ posicion: 42, nota: "FA_3_sostenido", sonido: "sonidos_piano_extra/3Fs.mp3" },
		{ posicion: 43, nota: "SOL_3", sonido: "sonidos_piano_extra/3G.mp3" },
		{ posicion: 44, nota: "SOL_3_sostenido", sonido: "sonidos_piano_extra/3Gs.mp3" },
		{ posicion: 45, nota: "LA_3", sonido: "sonidos_piano_extra/3A.mp3" },
		{ posicion: 46, nota: "LA_3_sostenido", sonido: "sonidos_piano_extra/3As.mp3" },
		{ posicion: 47, nota: "SI_3", sonido: "sonidos_piano_extra/3B.mp3" },
		{ posicion: 48, nota: "DO_4", sonido: "sonidos_piano_extra/4C.mp3" },
		{ posicion: 49, nota: "DO_4_sostenido", sonido: "sonidos_piano_extra/4Cs.mp3" },
		{ posicion: 50, nota: "RE_4", sonido: "sonidos_piano_extra/4D.mp3" },
		{ posicion: 51, nota: "RE_4_sostenido", sonido: "sonidos_piano_extra/4Ds.mp3" },
		{ posicion: 52, nota: "MI_4", sonido: "sonidos_piano_extra/4E.mp3" },
		{ posicion: 53, nota: "FA_4", sonido: "sonidos_piano_extra/4F.mp3" },
		{ posicion: 54, nota: "FA_4_sostenido", sonido: "sonidos_piano_extra/4Fs.mp3" },
		{ posicion: 55, nota: "SOL_4", sonido: "sonidos_piano_extra/4G.mp3" },
		{ posicion: 56, nota: "SOL_4_sostenido", sonido: "sonidos_piano_extra/4Gs.mp3" },
		{ posicion: 57, nota: "LA_4", sonido: "sonidos_piano_extra/4A.mp3" },
		{ posicion: 58, nota: "LA_4_sostenido", sonido: "sonidos_piano_extra/4As.mp3" },
		{ posicion: 59, nota: "SI_4", sonido: "sonidos_piano_extra/4B.mp3" },
		{ posicion: 60, nota: "DO_5", sonido: "sonidos_piano_extra/5C.mp3" },
		{ posicion: 61, nota: "DO_5_sostenido", sonido: "sonidos_piano_extra/5Cs.mp3" },
		{ posicion: 62, nota: "RE_5", sonido: "sonidos_piano_extra/5D.mp3" },
		{ posicion: 63, nota: "RE_5_sostenido", sonido: "sonidos_piano_extra/5Ds.mp3" },
		{ posicion: 64, nota: "MI_5", sonido: "sonidos_piano_extra/5E.mp3" },
		{ posicion: 65, nota: "FA_5", sonido: "sonidos_piano_extra/5F.mp3" },
		{ posicion: 66, nota: "FA_5_sostenido", sonido: "sonidos_piano_extra/5Fs.mp3" },
		{ posicion: 67, nota: "SOL_5", sonido: "sonidos_piano_extra/5G.mp3" },
		{ posicion: 68, nota: "SOL_5_sostenido", sonido: "sonidos_piano_extra/5Gs.mp3" },
		{ posicion: 69, nota: "LA_5", sonido: "sonidos_piano_extra/5A.mp3" },
		{ posicion: 70, nota: "LA_5_sostenido", sonido: "sonidos_piano_extra/5As.mp3" },
		{ posicion: 71, nota: "SI_5", sonido: "sonidos_piano_extra/5B.mp3" },
		{ posicion: 72, nota: "DO_6", sonido: "sonidos_piano_extra/6C.mp3" },
		{ posicion: 73, nota: "DO_6_sostenido", sonido: "sonidos_piano_extra/6Cs.mp3" },
		{ posicion: 74, nota: "RE_6", sonido: "sonidos_piano_extra/6D.mp3" },
		{ posicion: 75, nota: "RE_6_sostenido", sonido: "sonidos_piano_extra/6Ds.mp3" },
		{ posicion: 76, nota: "MI_6", sonido: "sonidos_piano_extra/6E.mp3" },
		{ posicion: 77, nota: "FA_6", sonido: "sonidos_piano_extra/6F.mp3" },
		{ posicion: 78, nota: "FA_6_sostenido", sonido: "sonidos_piano_extra/6Fs.mp3" },
		{ posicion: 79, nota: "SOL_6", sonido: "sonidos_piano_extra/6G.mp3" },
		{ posicion: 80, nota: "SOL_6_sostenido", sonido: "sonidos_piano_extra/6Gs.mp3" },
		{ posicion: 81, nota: "LA_6", sonido: "sonidos_piano_extra/6A.mp3" },
		{ posicion: 82, nota: "LA_6_sostenido", sonido: "sonidos_piano_extra/6As.mp3" },
		{ posicion: 83, nota: "SI_6", sonido: "sonidos_piano_extra/6B.mp3" },
		{ posicion: 84, nota: "DO_7", sonido: "sonidos_piano_extra/7C.mp3" }
		];
		​
	}

}

