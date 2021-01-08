package com.proyecto.service;

import java.util.HashMap;

import com.proyecto.model.entity.Usuario;

public interface IUsuarioService 
{
	public Usuario findByUsername(String username);
	
	public HashMap<String, Object> llenarSelect2(
		String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina
	);
}
