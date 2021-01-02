package com.proyecto.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.auth.component.JwtTokenUtil;
import com.proyecto.model.entity.Usuario;


@RestController
@CrossOrigin
public class LoginController
{

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	
	public class RetornoJwt
	{
		private String token;
		private String message;
		private Date expiration;
		
		
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Date getExpiration() {
			return expiration;
		}
		public void setExpiration(Date expiration) {
			this.expiration = expiration;
		}
	}
	
	
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public Object generateAuthenticationToken(HttpServletRequest request) throws Exception 
	{ 
		RetornoJwt retorno = new RetornoJwt();

		try { 
			
			String username = request.getParameter("username");   // Tendra un valor en caso de que el formato de envio sea   form-data
			String password = request.getParameter("password");   // Tendra un valor en caso de que el formato de envio sea   form-data
			
			if(username == null || password == null)   
			{
				// Si los datos enviados por el usuario estan con un contentType de tipo   application/json    de tipo raw (en bruto). Ejemplo:   {"username": "andres", "password": 12345}  
				
				Usuario usuario = usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);  // Aca es a la inversa. Se convierte un json a un objeto
					
				username = usuario.getUsername();    // Tendra un valor en caso de que el formato de envio sea   request InputStream (raw)
				password = usuario.getPassword();    // Tendra un valor en caso de que el formato de envio sea   request InputStream (raw)
			}

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(username);
			String token = jwtTokenUtil.crearToken(userDetails);   
			
			retorno.setToken(token);
			retorno.setMessage( String.format("Hola %s, has iniciado sesión con éxito", userDetails.getUsername()) );
			retorno.setExpiration( jwtTokenUtil.obtenerFechaExpiracionToken(token) );
			
			return ResponseEntity.ok(retorno); 
		} 
		catch (DisabledException ex) {
			retorno.setMessage( jwtTokenUtil.obtenerMensajeUsuarioDeshabilitado() );
		} 
		catch (BadCredentialsException ex) {
			retorno.setMessage( jwtTokenUtil.obtenerMensajeErrorAutenticacion() );
		}
		catch(Exception ex) { 
			retorno.setMessage( jwtTokenUtil.obtenerMensajeErrorInterno() );
		} 

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(retorno); 
	} 
	
	
	/*
	// Login
	$.ajax({
		url: "http://localhost:8080/api/login",
		type: "POST",
		data: JSON.stringify({"username":"admin","password":"123456789"}),
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (data)
		{
			console.log(JSON.stringify(data));
		}
	}); 
	
	// Listar 
	$.ajax({
		headers: {"Authorization": "Bearer " + token},
		url: "http://localhost:8080/api/MaestroRegion",
		type: "GET",
		success: function (data)
		{
			console.log(JSON.stringify(data));
		}
	});
	*/
}
