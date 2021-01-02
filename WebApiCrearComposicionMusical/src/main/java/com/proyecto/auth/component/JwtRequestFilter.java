package com.proyecto.auth.component;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.proyecto.auth.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter 
{

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	

	protected boolean requiresAuthentication(String header)   // Este metodo revisa si necesita ser autenticado
	{
		if (header == null || ! header.startsWith("Bearer ")) {
			return false;
		}
		return true;
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException 
	{
		try {
			
			String header = request.getHeader("Authorization");
	
			String username = null;
			String jwtToken = null;
			
			
			if (header != null && header.startsWith("Bearer "))   // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
			{
				jwtToken = header.replace("Bearer ", "");  // Mi cosecha. Al header "Bearer 123456"   lo deja asi:   "123456"   
				
				try {
					username = jwtTokenUtil.obtenerUsernameToken(jwtToken);
				} 
				catch (IllegalArgumentException ex) {
					System.out.println("Unable to get JWT Token");
				} 
				catch (ExpiredJwtException ex) {
					request.setAttribute("error_filtro_excepciones", jwtTokenUtil.obtenerMensajeTokenExpirado());   // Esto es capturado por la clase  JwtAuthenticationEntryPoint
					throw new ServletException();
				}
			} 
			else 
			{
				//System.out.println("JWT Token does not begin with Bearer String");
			}
	
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)  // Si el usuario realizo el login
			{
				UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
	
				if (jwtTokenUtil.validarToken(jwtToken, userDetails))  // if token is valid configure Spring Security to manually set authentication
				{
					Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();   // Asi era originalmente
	
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
					
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					// After setting the Authentication in the context, we specify that the current user is authenticated. So it passes the Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			chain.doFilter(request, response);
			
		} 
		catch (Exception ex) 
		{
			//ex.printStackTrace();
		}
	}
	

}
