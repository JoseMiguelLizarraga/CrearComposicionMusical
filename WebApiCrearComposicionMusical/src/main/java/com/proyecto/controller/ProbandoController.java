package com.proyecto.controller;

import java.util.Arrays;
import java.util.HashMap;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/probando")
public class ProbandoController 
{
	@Autowired private SessionFactory sessionFactory; 
	
	@GetMapping
	public Object listar()  
	{ 
		HashMap<String, Object> mapa = new HashMap<>(); 	
		mapa.put("info", Arrays.asList("has entrado a una area comun")); 

		return mapa; 
	} 
	
	
	@GetMapping(value = "/prohibido")
	public Object prohibido()  
	{ 
		try { 
			HashMap<String, Object> mapa = new HashMap<>(); 	
			mapa.put("info", Arrays.asList("has entrado a prohibido")); 

			return mapa; 
		} 
		catch (Exception ex) { 
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR); 
		} 
	} 
	
	
	
	
}
