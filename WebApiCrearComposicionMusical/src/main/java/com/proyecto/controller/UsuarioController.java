package com.proyecto.controller; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.service.IUsuarioService; 


@CrossOrigin 
@RestController 
@RequestMapping("/api/Usuario") 
public class UsuarioController 
{ 
	
	@Autowired 
	private IUsuarioService servicio; 
	

	@RequestMapping(value = "/llenarSelect2") 
	public @ResponseBody Object llenarSelect2(String clase, String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		try { 
			return servicio.llenarSelect2(atributoBuscado, busqueda, registrosPorPagina, numeroPagina); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 

	} 

} 



