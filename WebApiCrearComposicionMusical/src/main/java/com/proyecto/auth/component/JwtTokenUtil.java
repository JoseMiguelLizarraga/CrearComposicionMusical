package com.proyecto.auth.component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.model.repository.ParametrosLoginRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil  
{
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Autowired 
	private ParametrosLoginRepository parametrosLoginRepository;
	
	//=============================================================>>>>>>
	
	public String obtenerMensajeErrorAutenticacion() {
		return parametrosLoginRepository.findById(1).orElse(null).getMensajeErrorAutenticacion();
	}
	
	private int obtenerDuracionTokenMilisegundos() {
		return parametrosLoginRepository.findById(1).orElse(null).getDuracionTokenMilisegundos();
	}
	
	public String obtenerMensajeTokenExpirado() {
		return parametrosLoginRepository.findById(1).orElse(null).getMensajeTokenExpirado();
	}

	public String obtenerMensajeUsuarioDeshabilitado() {
		return parametrosLoginRepository.findById(1).orElse(null).getMensajeUsuarioDeshabilitado();
	}
	
	public String obtenerMensajeErrorInterno() {
		return parametrosLoginRepository.findById(1).orElse(null).getMensajeErrorInterno();
	}
	
	//=============================================================>>>>>>
	
	public String obtenerUsernameToken(String token) 
	{
		return obtenerClaimsToken(token, Claims::getSubject);
	}
	
	public <T> T obtenerClaimsToken(String token, Function<Claims, T> claimsResolver) 
	{
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}
	
	private Boolean estaExpiradoElToken(String token) 
	{
		Date expiration = obtenerFechaExpiracionToken(token);
		return expiration.before(new Date());
	}
	
	public Date obtenerFechaExpiracionToken(String token) 
	{
		return obtenerClaimsToken(token, Claims::getExpiration);
	}

	
	public String crearToken(UserDetails userDetails) throws IOException       // Crear el token
	{
		//long milisegundosPorHora = 3600000;  // 3.600.000  equivale a una hora

		String username = userDetails.getUsername();
		
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();   // Esto es para obtener los claims
		Claims claims = Jwts.claims();
		
		// Coloca los roles convertidos a json. 
		// Es usado por la clase   JWTAuthorizationFilter   en el metodo    doFilterInternal    en cada peticion http    para asi obtener los roles del usuario
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles) );  
		
		// Se crea el Json Web Token
		
		String token = Jwts.builder()
				.setClaims(claims)          				  // Se le pasan los claims, que contienen los roles del usuario
				.setSubject(username)        				  // Se le pasa el nombre del usuario
				.signWith(SignatureAlgorithm.HS512, secret)   // Se firma el token    signWith( algoritmo_de_encriptacion , clave_secreta )
				.setIssuedAt(new Date())   					  // Fecha de creacion del token
				
				//.setExpiration(new Date(System.currentTimeMillis() + 10000))    					// El token tendra solo 10 segundos antes de expirar
				//.setExpiration(new Date(System.currentTimeMillis() + milisegundosPorHora * 24))   // Fecha de expiracion del token. Sera en 24 horas
				.setExpiration(new Date( System.currentTimeMillis() + this.obtenerDuracionTokenMilisegundos() ))
				.compact();
		
		return token;
	}
	
	public Boolean validarToken(String token, UserDetails userDetails) 
	{
		final String username = obtenerUsernameToken(token);
		return (username.equals(userDetails.getUsername()) && ! estaExpiradoElToken(token));
	}

	/*
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}
	*/
}
