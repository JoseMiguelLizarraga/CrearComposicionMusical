package com.proyecto.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.proyecto.model.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	public Usuario findByUsername(String username);
}
