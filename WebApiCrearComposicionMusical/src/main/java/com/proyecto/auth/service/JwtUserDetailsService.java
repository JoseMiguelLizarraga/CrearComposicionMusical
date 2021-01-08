package com.proyecto.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.model.entity.Usuario;
import com.proyecto.model.entity.UsuarioCargo;
import com.proyecto.model.repository.UsuarioRepository;


@Service
public class JwtUserDetailsService implements UserDetailsService 
{

	@Autowired private UsuarioRepository usuarioRepository;  
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		
        Usuario usuario = usuarioRepository.findByUsername(username);
        
        if(usuario == null) {
        	throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for (UsuarioCargo c : usuario.getListaUsuarioCargo()) {
        	authorities.add(new SimpleGrantedAuthority(c.getCargo().getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }
        
        return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, authorities);
        
		//return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	/*
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		if ("techgeeknext".equals(username)) {
			return new User("techgeeknext", "$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi",
					new ArrayList<>());
		} 
		else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	*/

}