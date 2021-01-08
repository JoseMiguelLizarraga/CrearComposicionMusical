package com.proyecto.model.repository; 

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.CrudRepository; 
import org.springframework.data.repository.query.Param; 
import java.util.List;
import com.proyecto.model.entity.CabeceraComposicionMusical;
import com.proyecto.model.entity.DetalleComposicionMusical; 


public interface DetalleComposicionMusicalRepository extends CrudRepository<DetalleComposicionMusical, Integer>  
{ 
	
	@Query("select c from DetalleComposicionMusical c " 
		+ "where c.cabeceraComposicionMusical.id = :#{#cabeceraComposicionMusical.id}"  
	) 
	public List<DetalleComposicionMusical> findAllByCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical);  // Buscar todo lo que pertenece a la entidad CabeceraComposicionMusical 


	@Modifying 
	@Query("delete from DetalleComposicionMusical where cabeceraComposicionMusical.id = :id") 
	void deleteByCabeceraComposicionMusicalId(@Param("id") int id);   // Borrar todo lo que pertenece a la entidad CabeceraComposicionMusical 

	
	@Modifying 
	@Query("delete from DetalleComposicionMusical where cabeceraComposicionMusical.id = :#{#cabeceraComposicionMusical.id} and id not in :ids") 
	void deleteByCabeceraComposicionMusicalAndIdNotIn( 
		@Param("cabeceraComposicionMusical") CabeceraComposicionMusical cabeceraComposicionMusical, 
		@Param("ids") List<Integer> ids 
	); 

}
