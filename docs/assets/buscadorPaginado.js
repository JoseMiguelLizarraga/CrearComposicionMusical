
/*
import { Component, OnInit, AfterViewInit, NgZone } from "@angular/core"; 

constructor(private http:HttpClient, private config: ConfiguracionesProyectoService, private ngZone: NgZone) {  
	this.url = config.rutaWebApi;
} 

ngAfterViewInit() 
{
	selectBuscador({
		querySelector: "#buscador_productoCcnu_select2",
		placeholder: "Seleccione",
		url: this.url + "/Producto/llenarSelect2",
		params: { "clase": "ProductoCcnu", "atributoBuscado": "descripcion" },
		registrosPorPagina: {nombre: "registrosPorPagina", valor: 10},
		nombrePagina: "numeroPagina", nombreBusqueda: "busqueda", mensajeBuscando: "buscando resultados ...",
		optionInicial: {id: "", text: "todos", selected: true},
		optionSelected: { id: this.producto["id"], text: this.producto["descripcion"] },
		onchange: function () {  
			//console.log("El valor seleccionado es: " + event.target["value"]);
			window["resetearPaginacion"].zone.run(() => { window["resetearPaginacion"].loadAngularFunction(); }); 
			window["obtenerListaPrincipal"].zone.run(() => { window["obtenerListaPrincipal"].loadAngularFunction(); });
		}
	});
}
*/

function selectBuscador(datos)
{
	var busqueda = "";   			
	var consulta = datos["params"];
	var select = null;
	var optionInicialEncontrado = null;   // NUEVO OK

	if (datos["querySelector"] != null && datos["querySelector"] != "")   // Si se recibio un query selector. Ejemplo  #buscador
	{
		select = document.querySelector( datos["querySelector"] );
	}
	else if (datos["objetoHTML"] != null && datos["objetoHTML"] instanceof Object && datos["objetoHTML"].localName == "select")  // Si se recibio un objeto html y es un select
	{
		select = datos["objetoHTML"];
	}
	else {
		return console.log("No se recibio un querySelector o un objetoHTML para saber que select ocupar en el buscador");
	}

	var divPadre = select.parentElement;
	var input = Array.from(divPadre.childNodes).find(c=> c.localName != null && c.localName == "input");

	var mensajeBuscando = (datos["mensajeBuscando"] != null) ? datos["mensajeBuscando"] : "buscando ...";

	if (select.getAttribute("pagina") == null)  // Si aun no ha sido inicializado el buscador
	{   	
		select.setAttribute("pagina", "1");    	  
		select.setAttribute("estaCompleto", "false");
	}

	function colocarPlaceholder()
	{
		if (optionInicialEncontrado != null) {
			select.appendChild(optionInicialEncontrado);  // NUEVO
		}
		
		/*
		if (datos["placeholder"] != null && datos["placeholder"] != "")   // Agrega opcion vacia al placeholder en caso de que se especifique
		{
			select.textContent = "";    // Borra todos los options del select
			
			var opcionVacia = document.createElement("option");
			opcionVacia.text = datos["placeholder"];
			opcionVacia.value = "";
			select.appendChild(opcionVacia);
		}
		*/
	}


	// Si al inicializar el select, se coloco un valor para ser agregado como selected
	if (datos["optionSelected"] != null && ! ["", null].includes(datos["optionSelected"]["id"]) && ! ["", null].includes(datos["optionSelected"]["text"]))   
	{
		var optionSeleccionado = document.createElement("option");
		optionSeleccionado.text = datos["optionSelected"]["text"];
		optionSeleccionado.value = datos["optionSelected"]["id"];
		optionSeleccionado.selected = true;
		//select.appendChild(optionSeleccionado);
		optionInicialEncontrado = optionSeleccionado;   // Asigna valor a variable declarada arriba

		//==================================================>>>>  NUEVO
		select.setAttribute("textoSeleccionado",datos["optionSelected"]["text"]);   // Almacena el texto del option seleccionado en caso de tener que regresar atras
		select.setAttribute("valorSeleccionado", datos["optionSelected"]["id"]);    // Almacena el valor del option seleccionado en caso de tener que regresar atras
		//==================================================>>>>  NUEVO

		colocarPlaceholder();
	}
	else {

		optionInicialEncontrado = Array.from(select.options).find(c=> c.selected == true);   // Asigna valor a variable declarada arriba

		if (optionInicialEncontrado != null) 
		{
			//==================================================>>>>  NUEVO
			select.setAttribute("textoSeleccionado", optionInicialEncontrado.text);   // Almacena el texto del option seleccionado en caso de tener que regresar atras
			select.setAttribute("valorSeleccionado", optionInicialEncontrado.value);    // Almacena el valor del option seleccionado en caso de tener que regresar atras
			//==================================================>>>>  NUEVO

			//console.log(optionInicialEncontrado);
			colocarPlaceholder();
		}
	}

	function consultarWebApi(consulta = {}) 
	{
		// Se colocan los registros por pagina en la consulta que sera enviada
		consulta[ datos["registrosPorPagina"]["nombre"] ] = datos["registrosPorPagina"]["valor"];

		// Coloca el numero de la pagina en la consulta que sera enviada. Lo hace usando el nombre especificado en los parametros
		var pagina = parseInt( select.getAttribute("pagina") );
		consulta[ datos["nombrePagina"] ] = pagina;   

		if (busqueda != "") {
			consulta[ datos["nombreBusqueda"] ] = busqueda;   // Si existe una busqueda, la coloca en la consulta que sera enviada
		}
		else {
			consulta[ datos["nombreBusqueda"] ] = null;   // Se vacia una posible busqueda previa
		}

		var parametros = "?" + Object.entries(consulta).map(c=> c.join("=")).join("&"); 
		var url = datos["url"] + parametros;

		var ultimaPosicionScroll = select.scrollTop;

		fetch(url, {
			method: "GET",
			headers: {
				Authorization: (datos["token"] != null) ? "Bearer " + datos["token"] : null
			}
		})
		.then(response => response.json())
		.then(data => 
		{
			//==================================================================>>>>>>

			input.disabled = false;    // Habilita el input de busquedas
			select.disabled = false;   // Habilita el select
			input.placeholder = "";  // Remueve el placeholder que dice   buscando ...  en el input

			input.style.display = "block";
			input.focus();

			//select.options[0].style.display = "none";    	// Desaparece el primer option del select
			select.style.position = "absolute";          	// El select se superpone a otros elementos de la pagina
			select.style.zIndex = "3000"; 					// El select se superpone a otros elementos de la pagina
			//select.size = "6"; 							// El select se agranda

			//==============================================>>>> ACA ESTA LO QUE DESCUADRA TODO  .. se soluciona agregando  select.style.width = "100%";  despues de cada evento del select

			select.style.width = (input.getBoundingClientRect().width) + "px";   // Ajustar las medidas del select a las medidas del input
			
			//==============================================>>>> ACA ESTA LO QUE DESCUADRA TODO  .. se soluciona agregando  select.style.width = "100%";  despues de cada evento del select

			//console.log(data);
			var resultados = data["Results"];
			var totalRegistros = data["Total"];
			
			if (resultados.length > 0)  // Si se recibieron registros
			{
				if (totalRegistros > resultados.length)  // Si hay una pagina siguiente
				{
					var alturaAnterior = select.size;
					select.size = (resultados.length - 1);   // Activa el scroll 

					if (select.size < alturaAnterior) {  // Si la actual altura del select es inferior a la altura anterior
						select.size = alturaAnterior;     // Deja el select con la altura anterior
					}
				}
				else if (resultados.length <= totalRegistros) {  // En caso de que los resultados encontrados no alcancen a completar una pagina
					select.size = (resultados.length + 1);   // No activa el scroll y acomoda la altura del select al numero de registros 
				}
				else if (resultados.length == 1 && pagina == 1) {  // En caso de que se encuentre solo 1 registro y se trate de la primera pagina
					select.size = 2;
				}
				else {
					select.size = (resultados.length + 1);   // Acomoda la altura del select al numero de registros
				}
			}

			//var optionInicial;
			//optionInicialEncontrado         // OCUPAR ESTO OK.

			// if (datos["optionInicial"] != null)   // Si existe un option inicial. Ej: opcion "todos"
			// {   
			// 	optionInicial = document.createElement("option");
			// 	optionInicial.text = datos["optionInicial"]["text"];
			// 	optionInicial.value = datos["optionInicial"]["id"];
			// 	optionInicial.selected = (datos["optionInicial"]["selected"] != null) ? datos["optionInicial"]["selected"] : false;
			// }

			if (input.value == null || input.value == "")   // Si el usuario no ha escrito nada en el input de busquedas
			{	
				
				if (select.value != null && select.value != "")   // Si existe un option seleccionado 
				{
					var optionSeleccionado = Array.from(select.options).find(c=> c.selected == true);
					
					if (pagina == 1)   // Si es la primera pagina
					{   
						select.textContent = "";    				// Borra todos los options del select

						if (optionInicialEncontrado != null && optionInicialEncontrado.value == "")   // Si existe un option inicial. Ej: opcion "todos"
						{  
							//select.appendChild(optionInicial);          // Coloca el option inicial al comienzo
							select.appendChild(optionInicialEncontrado);  // Coloca el option inicial al comienzo

							select.appendChild(optionSeleccionado);     // Agrega el option que estaba seleccionado antes
							select.scrollTo(0, 1);                      // Coloca el scroll del select en la posicion inicial         scrollTo(eje-horizontal, eje-vertical)
						}
						if (optionSeleccionado.value != "")   // Si se encontro un option seleccionado y no es el option por default   ...NUEVO OK
						{
							select.appendChild(optionSeleccionado);     // Agrega el option que estaba seleccionado antes
							select.scrollTo(0, 0);                      // Coloca el scroll del select en la posicion inicial         scrollTo(eje-horizontal, eje-vertical)
						}
						// else {
						// 	
						// 	select.scrollTo(0, 0);                      // Coloca el scroll del select en la posicion inicial         scrollTo(eje-horizontal, eje-vertical)
						// }
					}

					data["Results"].filter(c=> c.id != optionSeleccionado.value).forEach(c=> {   // Pone el resto de los options con excepcion del que ya estaba seleccionado
						var option = document.createElement("option");
						option.text = c["text"];
						option.value = c["id"];
						select.appendChild(option);
					});
				}
				else   // Si no existe un option seleccionado
				{
					if (pagina == 1) {  // Si es la primera pagina
						select.textContent = "";    // Borra todos los options del select

						if (optionInicialEncontrado != null) {  		// Si existe un option inicial. Ej: opcion "todos"
							//select.appendChild(optionInicial);      // Coloca el option inicial al comienzo
							select.appendChild(optionInicialEncontrado);      // Coloca el option inicial al comienzo
						}
					}

					data["Results"].forEach(c=> {
						var option = document.createElement("option");
						option.text = c["text"];
						option.value = c["id"];
						//if (c.id == 3) { option.selected = true; }
						select.appendChild(option);
					});
				}
			}
			else   // Si el usuario escribio en el input de busquedas
			{
				//select.textContent = "";    // Borra todos los options del select

				data["Results"].forEach(c=> {
					var option = document.createElement("option");
					option.text = c["text"];
					option.value = c["id"];
					//if (c.id == 3) { option.selected = true; }
					select.appendChild(option);
				});
			}

			//===============================================================================>>>>>>>
			// Manejar el scroll
			if (pagina > 1)   // Si no es la primera pagina
			{ 
				select.scrollTop = ultimaPosicionScroll;   
				//select.scrollTo({top: ultimaPosicionScroll, left: 100, behavior: 'smooth'});
			}

			//console.log("Total: " + data.Total + " --- Cantidad options: " + select.options.length);
			//===============================================================================>>>>>>>

			if (select.options.length >= totalRegistros) {          // Si se completo el select con los registros buscados
				select.setAttribute("estaCompleto", "true");  		// Deshabilita uso del scroll
			}

		})
		.catch(function (error) {
			console.log("Se encontró un error", error)
		});
	
	};

	select.addEventListener("change", (event) =>   // Si se selecciono un option del select
	{
		input.style.display = "none";     // Desaparece el input para buscar

		select.style.position = "static";          		// El select ya no se superpone a otros elementos de la pagina
		select.style.zIndex = "1"; 						// El select ya no se superpone a otros elementos de la pagina
		select.removeAttribute("size");           		// El select se achica	
		select.setAttribute("pagina", "1");             // Deja en 1 el atributo pagina, para que en la siguiente busqueda comience desde el inicio
		select.style.width = "100%";                    // IMPORTANTE. ESTO IMPIDE QUE SE DESCUADREN OTROS ELEMENTOS DE LA PAGINA

		//==================================================>>>>  NUEVO
	
		var optionSeleccionado = Array.from(select.options).find(c=> c["selected"] == true);   // Busca el option seleccionado

		select.setAttribute("textoSeleccionado", optionSeleccionado.text);    // Almacena el texto del option seleccionado en caso de tener que regresar atras
		select.setAttribute("valorSeleccionado", optionSeleccionado.value);   // Almacena el valor del option seleccionado en caso de tener que regresar atras

		//==================================================>>>>  NUEVO

		// Si en el evento onchange se coloco una funcion, se ejecuta dicha funcion
		if (datos["onchange"] != null && typeof datos["onchange"] === "function") {  datos["onchange"]();  }
		//if (select.value != null && select.value != "" && datos["onchange"] != null && typeof datos["onchange"] === "function") {  datos["onchange"]();  }
	});
	
	select.addEventListener("scroll", (event) => 
	{
		if (input.style.display == "block")  // Esto es para prevenir que se ejecute al hacer click fuera del buscador
		{
			// select.setAttribute("estaCompleto", "false");  // Habilita uso del scroll

			if (select.getAttribute("estaCompleto") == "false")   // Si aun existen resultados para ser encontrados en el select
			{
				// if(event.target.offsetHeight + event.target.scrollTop == event.target.scrollHeight)  // Asi era antes

				if(event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight)  // Si se llego al fin del scroll
				{
					if (input.value == null || input.value == "") {   // Si no hay ninguna busqueda en la caja de busquedas
						input.placeholder = mensajeBuscando;  		  // "buscando ...";
					}
					
					input.disabled = true;        // Deshabilita el input de busquedas
					select.disabled = true;       // Deshabilita el select

					var paginaActual = parseInt( select.getAttribute("pagina") );
					var nuevaPagina = paginaActual + 1;
					select.setAttribute("pagina", nuevaPagina);   
					consultarWebApi(consulta);
					//event.target.scrollTo({top: 100, left: 100, behavior: 'smooth'});
				}
			}
		}
	});

	select.addEventListener("mousedown", (event) =>    // Impedir que el select se abra al hacer click
	{  
		if (event.target.localName == "select")     
		{
			event.preventDefault(); 

			if (select.value == null || select.value == "") {   // Si ningun option ha sido seleccionado en el select
				//select.options[0].text = mensajeBuscando;  	    // "buscando ...";
			}  
		}
	});   
	
	
	select.addEventListener("click", (event) =>   // Si se hace click sobre el select
	{
		busqueda = "";
		select.setAttribute("estaCompleto", "false");  // Habilita uso del scroll

		if (event.target.localName == "select")  // Esto es para impedir que se ejecute al momento de seleccionar un option del select    
		{
			input.value = "";    // Se vacia una posible busqueda previa en el input
			consultarWebApi(consulta);
		}
	});
	
	input.addEventListener("keyup", function(event)   // Si se escribio en el txt para busquedas
	{
		select.setAttribute("estaCompleto", "false");  // Habilita uso del scroll

		if (event.target.value != null && event.target.value != "") {  
			busqueda = event.target.value;    // Agrega la busqueda
			select.setAttribute("pagina", "1");
		}    
		if (event.target.value == null || event.target.value == "") {  
			busqueda = "";       // Anula la busqueda
		}   				

		select.textContent = "";    				// Borra todos los options del select
		consultarWebApi(consulta);
	});
	
	
	window.addEventListener("click", function(e) 
	{   
		if (! divPadre.contains(e.target))  // Si se hizo click fuera del buscador
		{
			var optionSeleccionado_Usuario = Array.from(select.options).find(c=> c.selected == true && c.value != "");  // Esto debe estar antes del resto para que funcione

			input.disabled = false;    // Habilita el input de busquedas en caso de que haya sido deshabilitado
			select.disabled = false;   // Habilita el select en caso de que haya sido deshabilitado
			input.placeholder = "";    // Remueve el placeholder que dice   buscando ...  en el input

			//console.log("Salir del divPadre");
			input.style.display = "none";    					// Desaparece el input para buscar
			select.style.position = "relative";          		// El select ya no se superpone a otros elementos de la pagina
			select.style.zIndex = "1"; 							// El select ya no se superpone a otros elementos de la pagina
			select.removeAttribute("size");           			// El select se achica
			select.setAttribute("pagina", "1");                 // Deja en 1 el atributo pagina, para que en la siguiente busqueda comience desde el inicio
			select.setAttribute("estaCompleto", "false");  		// Habilita uso del scroll
			select.style.width = "100%";                        // IMPORTANTE. ESTO IMPIDE QUE SE DESCUADREN OTROS ELEMENTOS DE LA PAGINA

			if (select.value == null || select.value == "" || typeof optionSeleccionado_Usuario === "undefined")  // Si ningun option del select ha sido seleccionado
			{   
				colocarPlaceholder();  
			}  
		} 
	});
	
	window.addEventListener("resize", function(e) 
	{   
		if (input === document.activeElement)   // Si el input esta seleccionado y tiene el foco encima
		{ 
			select.style.width = (input.getBoundingClientRect().width) + "px";   // Ajustar las medidas del select a las medidas del input
		}
		else {
			select.style.width = "100%";
		}
	});


}
