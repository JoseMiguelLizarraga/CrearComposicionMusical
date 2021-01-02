package com.proyecto.model.repository;

import org.springframework.data.repository.CrudRepository;
import com.proyecto.model.entity.Usuario;


public interface IUsuarioRepository extends CrudRepository<Usuario, Long>
{
	public Usuario findByUsername(String username);
}
