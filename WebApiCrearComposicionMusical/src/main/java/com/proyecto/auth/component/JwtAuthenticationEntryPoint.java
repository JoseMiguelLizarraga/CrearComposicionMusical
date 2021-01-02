package com.proyecto.auth.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint 
{
	
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException 
	{
		String error = (String) request.getAttribute("error_filtro_excepciones");  // Esto viene de la clase  JwtRequestFilter

        if (error != null) 
        {
    		Map<String, Object> body = new HashMap<String, Object>();

    		body.put("error", error);                                        // body.put("error", "Se termino el tiempo limite de uso del usuario");
    		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    		
    		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);   			
    		response.setContentType("application/json");
        }
        else {
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
	}
}
