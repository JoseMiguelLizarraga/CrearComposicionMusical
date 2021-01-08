package com.proyecto.model.repository; 

import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.CrudRepository; 
import org.springframework.data.repository.query.Param; 
import java.util.List; 
import com.proyecto.model.entity.CabeceraComposicionMusical; 


public interface CabeceraComposicionMusicalRepository extends CrudRepository<CabeceraComposicionMusical, Integer>  
{ 
	@Query(
	"select new CabeceraComposicionMusical(" 
		+ "c.id, " 
		+ "usuario, " 
		+ "c.visible, " 
		+ "c.autor, " 
		+ "c.titulo" 
	+ ") " 
	+ "from CabeceraComposicionMusical c " 
		+ "inner join Usuario usuario on c.usuario.id = usuario.id "   // Obtiene informacion de la entidad padre Usuario  
	+ "where c.id = :id"
	) 
	public CabeceraComposicionMusical buscarPorId(@Param("id") int id); 
	

	@Query( 
	"select new CabeceraComposicionMusical(" 
		+ "c.id, " 
		+ "usuario, " 
		+ "c.visible, " 
		+ "c.autor, " 
		+ "c.titulo" 
	+ ") " 
	+ "from CabeceraComposicionMusical c " 
		+ "inner join Usuario usuario on c.usuario.id = usuario.id "   // Obtiene informacion de la entidad padre Usuario  
	) 
	public List<CabeceraComposicionMusical> findAll(); 

	//===========================================================>>>>> 
	//Consulta datatable 
	
	public String consultaTablaBusquedas = "where " 
		+ "(:idUsuario = 0 or c.usuario.id = :idUsuario) and " 
		+ "(:visible is null or c.visible = :visible) and " 
		+ "(:autor is null or c.autor like %:autor%) and " 
		+ "(:titulo is null or c.titulo like %:titulo%) "; 

	@Query("select count(c.id) from CabeceraComposicionMusical c " + consultaTablaBusquedas) 
	public long contarTotalRegistrosDataTable( 
		@Param("idUsuario") int idUsuario, 
		@Param("visible") Boolean visible, 
		@Param("autor") String autor, 
		@Param("titulo") String titulo
	); 

	@Query("select c from CabeceraComposicionMusical c " 
		+ "join fetch c.usuario "   // Obtiene informacion de la entidad padre Usuario 
		+ consultaTablaBusquedas 
		+ "order by c.id desc"  
	) 
	List<CabeceraComposicionMusical> llenarDataTable( 
		@Param("idUsuario") int idUsuario, 
		@Param("visible") Boolean visible, 
		@Param("autor") String autor, 
		@Param("titulo") String titulo, 
		Pageable pageable 
	); 
	
}
