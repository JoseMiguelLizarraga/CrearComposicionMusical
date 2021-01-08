package com.proyecto.model.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto.model.entity.CabeceraComposicionMusical;
import com.proyecto.model.entity.Usuario;


public interface UsuarioRepository extends CrudRepository<Usuario, Long>
{
	
	public Usuario findByUsername(String username);
	
	//Busqueda con select 2 
	
	@Query("select count(c.id) from Usuario c where (:atributoBuscado is null or c.nombre like %:atributoBuscado%)") 
	long contarTotalRegistros_Select_2(@Param("atributoBuscado") String atributoBuscado); 

	@Query("select c from Usuario c where (:atributoBuscado is null or c.nombre like %:atributoBuscado%)") 
	List<Usuario> llenarSelect_2(@Param("atributoBuscado") String atributoBuscado, Pageable pageable); 
	
}
