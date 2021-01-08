package com.proyecto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.proyecto.model.entity.CabeceraComposicionMusical;
import com.proyecto.model.entity.DetalleComposicionMusical;
import com.proyecto.model.entity.Usuario;
import com.proyecto.model.repository.CabeceraComposicionMusicalRepository;
import com.proyecto.model.repository.DetalleComposicionMusicalRepository;
import com.proyecto.model.repository.UsuarioRepository;
import com.proyecto.service.ICabeceraComposicionMusicalService;
import com.proyecto.service.IDaoGenericoService; 


@Service  
public class CabeceraComposicionMusicalServiceImpl implements ICabeceraComposicionMusicalService
{ 

	@Autowired 
	private SessionFactory sessionFactory; 
	
	@Autowired 
	private UsuarioRepository usuarioRepository; 
	
	@Autowired 
	private IDaoGenericoService daoGenerico;
	
	@Autowired private CabeceraComposicionMusicalRepository repositorio;
	@Autowired private DetalleComposicionMusicalRepository repositorio_DetalleComposicionMusical;
	
	
	@Override
	public Usuario obtenerUsuarioLogueado()
	{
		if (
		 SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
		 !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) &&
		 SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
		) 
		{
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return usuarioRepository.findByUsername(userDetails.getUsername());
		}

		return null;
	}
	
	
	@Override
	public Object obtenerListasBuscador()  
	{ 	
		HashMap<String, Object> mapa = new HashMap<>(); 
		mapa.put("listaUsuario", daoGenerico.findAll("select new map(c.id as id, c.apellidoPaterno as apellidoPaterno, c.visible as visible, c.nombre as nombre, c.activo as activo, c.largoPassword as largoPassword, c.telefono as telefono, c.password as password, c.rut as rut, c.username as username, c.apellidoMaterno as apellidoMaterno) from Usuario c")); 

		return mapa; 
	} 
	
	
	@Override
	public CabeceraComposicionMusical buscarPorId(int id) 
	{ 
		try { 
			CabeceraComposicionMusical cabeceraComposicionMusical = repositorio.buscarPorId(id); 

			List<DetalleComposicionMusical> listaDetalleComposicionMusical = repositorio_DetalleComposicionMusical.findAllByCabeceraComposicionMusical(cabeceraComposicionMusical); 

			cabeceraComposicionMusical.setListaDetalleComposicionMusical(listaDetalleComposicionMusical);  // Agrega referencias cruzadas de la entidad DetalleComposicionMusical  

			return cabeceraComposicionMusical; 
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al buscar CabeceraComposicionMusical con el id: " + id + "." + ex.getMessage()); 
		} 
	} 


//	@Override
//	public CabeceraComposicionMusical buscarPorId(int id) 
//	{ 
//		Session sesion = sessionFactory.openSession(); 
//
//		try { 
//			CabeceraComposicionMusical cabeceraComposicionMusical =  (CabeceraComposicionMusical) sesion.createQuery( 
//				"select c from CabeceraComposicionMusical c " + 
//				"join fetch c.usuario " +   // Obtiene informacion de la entidad padre Usuario 
//				"where c.id = " + id 
//			).uniqueResult(); 
//
//			List<DetalleComposicionMusical> listaDetalleComposicionMusical = sesion.createQuery( 
//				"select c from DetalleComposicionMusical c " 
//				+ "where c.cabeceraComposicionMusical.id = " + id 
//			).list(); 
//
//			cabeceraComposicionMusical.setListaDetalleComposicionMusical(listaDetalleComposicionMusical); 
//
//			return cabeceraComposicionMusical; 
//		} 
//		catch(Exception ex) { 
//			throw new RuntimeException("Error al buscar CabeceraComposicionMusical con el id: " + id + "." + ex.getMessage()); 
//		} 
//		finally { 
//			sesion.close(); 
//		} 
//	} 

	
	/*
	@Override
	public <T> Object llenarSelect2(Class<T> claseEntidad, String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		Session session = sessionFactory.openSession(); 

		try { 
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); 
			CriteriaQuery<T> cr = criteriaBuilder.createQuery(claseEntidad); 
			Root<T> root = cr.from(claseEntidad); 
			cr.select(root); 
			List<Predicate> predicates = new ArrayList<Predicate>(); 

			if (StringUtils.hasText(busqueda)) 
			{ 
				if (busqueda.matches("[0-9]*") && ! busqueda.contains(".")) {   // Si es numerico 
					predicates.add(criteriaBuilder.equal(root.get(atributoBuscado), Integer.parseInt(busqueda))); 
				} 
				else {   
					predicates.add(criteriaBuilder.like(root.get(atributoBuscado), "%" + busqueda + "%")); 
				} 
			} 

			cr.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {}))); 
			int totalRegistros = session.createQuery(cr).getResultList().size(); 

			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("Total", totalRegistros); 

			int inicio = 0; 
			if( numeroPagina != 1) {  inicio = (numeroPagina - 1) * registrosPorPagina;  } 

			List<T> lista = session.createQuery(cr) 
				.setFirstResult(inicio) 				// Delimito la busqueda con el inicio 
				.setMaxResults(registrosPorPagina) 	    // Delimito la busqueda con el total de registros 
				.getResultList(); 

			mapa.put("Results", new ArrayList<Object>() 
			{{ 
				lista.forEach(objeto-> 
				{ 
					if (objeto.getClass().getName().contains("Usuario")) { 
						Usuario c = (Usuario) objeto; 
						add(new HashMap<String, Object>() {{ put("id", c.getId()); put("text", c.getId().toString()); }}); 
					} 
				}); 
			}}); 

			return mapa; 
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al realizar busqueda select2. " + ex.getMessage()); 
		} 
		finally { 
			session.close(); 
		} 
	} 
	*/
	
	@Override
	public List<CabeceraComposicionMusical> listar() 
	{ 
		try { 
			return repositorio.findAll(); 
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al listar la entidad CabeceraComposicionMusical. " + ex.getMessage()); 
		} 
	} 

	
//	@Override
//	public List<CabeceraComposicionMusical> listar() 
//	{ 
//		Session sesion = sessionFactory.openSession(); 
//
//		try { 
//			return sesion.createQuery( 
//				"select c from CabeceraComposicionMusical c " 
//				+ "join fetch c.usuario "   // Obtiene informacion de la entidad padre Usuario 
//			).list(); 
//		} 
//		catch(Exception ex) { 
//			throw new RuntimeException("Error al listar la entidad CabeceraComposicionMusical. " + ex.getMessage()); 
//		} 
//		finally { 
//			sesion.close(); 
//		} 
//	} 
	
	
	@Override
	public HashMap<String, Object> llenarDataTableCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical, int inicio, int registrosPorPagina) 
	{ 
		try { 
			Usuario usuarioLogueado = obtenerUsuarioLogueado();
			
			int idUsuarioBuscado = (cabeceraComposicionMusical.getUsuario() != null && cabeceraComposicionMusical.getUsuario().getId() != null) ?
				cabeceraComposicionMusical.getUsuario().getId() : 
				(usuarioLogueado != null) ?
					usuarioLogueado.getId() : 
					0;	

			int pagina = (inicio > 0) ? inicio / registrosPorPagina : inicio;  // Obtener la pagina en base a la posicion enviada por el datatable desde el frontend 
			
			List<CabeceraComposicionMusical> listaCabeceraComposicionMusical = repositorio.llenarDataTable( 
				idUsuarioBuscado, 
				cabeceraComposicionMusical.getVisible(), 
				cabeceraComposicionMusical.getAutor(), 
				cabeceraComposicionMusical.getTitulo(), 
				PageRequest.of(pagina, registrosPorPagina) 
			); 

			long totalRegistros = repositorio.contarTotalRegistrosDataTable( 
				idUsuarioBuscado, 
				cabeceraComposicionMusical.getVisible(), 
				cabeceraComposicionMusical.getAutor(), 
				cabeceraComposicionMusical.getTitulo() 
			); 

			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("recordsFiltered", totalRegistros); 
			mapa.put("recordsTotal", totalRegistros); 

			mapa.put("data", listaCabeceraComposicionMusical); 

			return mapa; 
		} 
		catch(Exception ex){ 
			throw new RuntimeException("Error al listar la entidad CabeceraComposicionMusical con el metodo llenarDataTable. " + ex.getMessage()); 
		} 
	} 

	
	/*
	@Override
	public HashMap<String, Object> llenarDataTableCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical, int inicio, int registrosPorPagina) 
	{ 
		Session session = sessionFactory.openSession(); 

		try{ 
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); 
			CriteriaQuery<CabeceraComposicionMusical> cr = criteriaBuilder.createQuery(CabeceraComposicionMusical.class); 
			Root<CabeceraComposicionMusical> root = cr.from(CabeceraComposicionMusical.class); 

			Fetch<CabeceraComposicionMusical, Usuario> usuario_Fetch = root.fetch("usuario", JoinType.INNER);   // Obtiene datos de la entidad Usuario 

			cr.select(root);  // Asi llama todos los atributos de la entidad 

			List<Predicate> predicates = new ArrayList<Predicate>(); 

			if (cabeceraComposicionMusical.getVisible() != null) { 
				predicates.add(criteriaBuilder.equal(root.get("visible"), cabeceraComposicionMusical.getVisible())); 
			} 
			if (StringUtils.hasText(cabeceraComposicionMusical.getAutor())) { 
				predicates.add(criteriaBuilder.like(root.get("autor"), "%" + cabeceraComposicionMusical.getAutor() + "%")); 
			} 
			if (StringUtils.hasText(cabeceraComposicionMusical.getTitulo())) { 
				predicates.add(criteriaBuilder.like(root.get("titulo"), "%" + cabeceraComposicionMusical.getTitulo() + "%")); 
			}			
			
//			if (cabeceraComposicionMusical.getUsuario() != null && cabeceraComposicionMusical.getUsuario().getId() != null) { 
//				Join<CabeceraComposicionMusical, Usuario> usuario_Join = (Join<CabeceraComposicionMusical, Usuario>) usuario_Fetch;   // Realiza join con la entidad Usuario a partir del fetch creado  
//				predicates.add(criteriaBuilder.equal(usuario_Join.get("id"), cabeceraComposicionMusical.getUsuario().getId() )); 
//			} 
			
			
			Usuario usuarioLogueado = obtenerUsuarioLogueado();
			
			if (usuarioLogueado != null) 
			{
				Join<CabeceraComposicionMusical, Usuario> usuario_Join = (Join<CabeceraComposicionMusical, Usuario>) usuario_Fetch;   // Realiza join con la entidad Usuario a partir del fetch creado  
				predicates.add(criteriaBuilder.equal(usuario_Join.get("id"), usuarioLogueado.getId() )); 
			}

			cr.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {}))); 

			int totalRegistros = session.createQuery(cr).getResultList().size(); 

			List<CabeceraComposicionMusical> listaCabeceraComposicionMusical = session.createQuery(cr) 
				.setFirstResult(inicio) 				// Delimito la busqueda con el inicio 
				.setMaxResults(registrosPorPagina) 	// Delimito la busqueda con el total de registros 
				.getResultList(); 

			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("recordsFiltered", totalRegistros); 
			mapa.put("recordsTotal", totalRegistros); 

			mapa.put("data", listaCabeceraComposicionMusical); 

			return mapa; 
		} 
		catch(Exception ex){ 
			throw new RuntimeException("Error al listar la entidad CabeceraComposicionMusical con el metodo llenarDataTable. " + ex.getMessage()); 
		} 
		finally{ 
			session.close(); 
		} 
	} 
	*/

	@Override
	@Transactional 
	public CabeceraComposicionMusical guardar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception 
	{ 
		this.validarCabeceraComposicionMusical(cabeceraComposicionMusical);  // Validación 

		try { 
			Usuario usuarioLogueado = obtenerUsuarioLogueado();
			cabeceraComposicionMusical.setUsuario(usuarioLogueado);   // Asi el creador de la composicion sera el usuario logueado
			
			repositorio.save(cabeceraComposicionMusical); 

			if (cabeceraComposicionMusical.getListaDetalleComposicionMusical() != null && cabeceraComposicionMusical.getListaDetalleComposicionMusical().size() > 0)  // DetalleComposicionMusical 
			{
				for(DetalleComposicionMusical detalle : cabeceraComposicionMusical.getListaDetalleComposicionMusical()) { 
					detalle.setCabeceraComposicionMusical(cabeceraComposicionMusical); 
					repositorio_DetalleComposicionMusical.save(detalle); 
				} 
			} 

			return cabeceraComposicionMusical; 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo insertar el registro. " + ex.getMessage()); 
		} 
	} 
	
	/*
	@Override
	public CabeceraComposicionMusical guardar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception 
	{ 
		validarCabeceraComposicionMusical(cabeceraComposicionMusical);  // Validación 

		Session sesion = null; 
		Transaction tx = null; 

		try { 
			Usuario usuarioLogueado = obtenerUsuarioLogueado();
			cabeceraComposicionMusical.setUsuario(usuarioLogueado);   // Asi el creador de la composicion sera el usuario logueado
			
			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 
			sesion.save(cabeceraComposicionMusical); 

			// Referencias Cruzadas 

			if (cabeceraComposicionMusical.getListaDetalleComposicionMusical().size() > 0)  // DetalleComposicionMusical 
			{
				for(DetalleComposicionMusical detalle : cabeceraComposicionMusical.getListaDetalleComposicionMusical()) { 
					detalle.setCabeceraComposicionMusical(cabeceraComposicionMusical); 
					sesion.save(detalle); 
				} 
			} 
			// Fin Referencias Cruzadas 

			tx.commit(); 
			sesion.close(); 
			return cabeceraComposicionMusical; 
		} 
		catch (Exception ex) 
		{ 
			tx.rollback(); 
			throw new RuntimeException("No se pudo insertar el registro. " + ex.getMessage()); 
		} 
	} 
	*/
	
	@Override
	@Transactional 
	public void actualizar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception 
	{ 
		this.validarCabeceraComposicionMusical(cabeceraComposicionMusical);  // Validación 

		try { 
			CabeceraComposicionMusical objeto = this.buscarPorId(cabeceraComposicionMusical.getId()); 
			
			if (obtenerUsuarioLogueado() != objeto.getUsuario()) {
				throw new RuntimeException("Usted no tiene permiso para realizar cambios en esta composicion"); 
			}

			//objeto.setUsuario(cabeceraComposicionMusical.getUsuario()); 
			objeto.setVisible(cabeceraComposicionMusical.getVisible()); 
			objeto.setAutor(cabeceraComposicionMusical.getAutor()); 
			objeto.setTitulo(cabeceraComposicionMusical.getTitulo()); 

			repositorio.save(objeto); 
			//repositorio.save(cabeceraComposicionMusical);  // Grabar directo como viene desde la vista 


			// Referencias Cruzadas de la entidad DetalleComposicionMusical 

			List<DetalleComposicionMusical> listaDetalleComposicionMusical = cabeceraComposicionMusical.getListaDetalleComposicionMusical(); 

			if (listaDetalleComposicionMusical != null && listaDetalleComposicionMusical.size() > 0) 
			{ 
				// Si solo se recibio un item y es nuevo 
				if (listaDetalleComposicionMusical.size() == 1 && listaDetalleComposicionMusical.stream().anyMatch(c-> c.getId() == null)) { 
					repositorio_DetalleComposicionMusical.deleteByCabeceraComposicionMusicalId(cabeceraComposicionMusical.getId()); 
				} 
				if (listaDetalleComposicionMusical.stream().anyMatch(c-> c.getId() != null))  // Si al menos un detalle trae id 
				{ 
					// Se borran los detalles almacenados cuyos ids no esten en los ids de los detalles actualizados 

					repositorio_DetalleComposicionMusical.deleteByCabeceraComposicionMusicalAndIdNotIn( 
						cabeceraComposicionMusical, 
						listaDetalleComposicionMusical.stream().filter(a-> a.getId() != null).map(c -> c.getId()).collect(Collectors.toList()) 
					); 

				} 
				for(DetalleComposicionMusical detalle : listaDetalleComposicionMusical) 
				{ 
					if (detalle.getId() != null)  // Si trae id significa que esta almacenado 
					{ 
						DetalleComposicionMusical detalleBuscado = repositorio_DetalleComposicionMusical.findById(detalle.getId()).orElse(null); 
						detalleBuscado.setCadenaListaSubDetalles(detalle.getCadenaListaSubDetalles()); 
						detalleBuscado.setOrden(detalle.getOrden()); 
						repositorio_DetalleComposicionMusical.save(detalleBuscado); 
					} 
					else   // Si no esta guardado el detalle recorrido, se agrega 
					{ 
						detalle.setCabeceraComposicionMusical(cabeceraComposicionMusical); 
						repositorio_DetalleComposicionMusical.save(detalle); 
					} 
				} 
			} 
			else  // Si no se recibieron detalles de la clase DetalleComposicionMusical 
			{ 
				repositorio_DetalleComposicionMusical.deleteByCabeceraComposicionMusicalId(cabeceraComposicionMusical.getId()); 
			} 

		} 
		catch (Exception ex) { 
			throw new RuntimeException("No se pudo actualizar el registro. " + ex.getMessage()); 
		} 
	} 
	

	/*
	@Override
	public void actualizar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception 
	{ 
		validarCabeceraComposicionMusical(cabeceraComposicionMusical);  // Validación 

		Session sesion = null; 
		Transaction tx = null; 

		try { 
			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 

			CabeceraComposicionMusical objeto = buscarPorId(cabeceraComposicionMusical.getId()); 

			objeto.setVisible(cabeceraComposicionMusical.getVisible()); 
			objeto.setAutor(cabeceraComposicionMusical.getAutor()); 
			objeto.setTitulo(cabeceraComposicionMusical.getTitulo()); 
			objeto.setUsuario(cabeceraComposicionMusical.getUsuario()); 

			sesion.update(objeto); 
			//sesion.update(cabeceraComposicionMusical);  // Grabar directo como viene desde la vista 


			// Referencias Cruzadas de la entidad DetalleComposicionMusical 

			List<DetalleComposicionMusical> listaDetalleComposicionMusical = cabeceraComposicionMusical.getListaDetalleComposicionMusical(); 

			if (listaDetalleComposicionMusical.size() > 0) 
			{ 
				// Si solo se recibio un item y es nuevo 
				if (listaDetalleComposicionMusical.size() == 1 && listaDetalleComposicionMusical.stream().anyMatch(c-> c.getId() == null)) { 
					sesion.createQuery("delete from DetalleComposicionMusical where cabeceraComposicionMusical.id = "+cabeceraComposicionMusical.getId()).executeUpdate(); 
				} 
				if (listaDetalleComposicionMusical.stream().anyMatch(c-> c.getId() != null))  // Si al menos un detalle trae id 
				{ 
					// Se borran los detalles almacenados cuyos ids no esten en los ids de los detalles actualizados 
					List<DetalleComposicionMusical> detallesEliminar = sesion.createQuery("from DetalleComposicionMusical where cabeceraComposicionMusical.id = :cabeceraComposicionMusicalId and id not in :ids") 
					.setParameter("cabeceraComposicionMusicalId", cabeceraComposicionMusical.getId()) 
					.setParameterList("ids", listaDetalleComposicionMusical.stream().filter(a-> a.getId() != null).map(c -> c.getId()).collect(Collectors.toList())) 
					.list(); 

					for(DetalleComposicionMusical detalle : detallesEliminar) {  sesion.delete(detalle);  } 

				} 
				for(DetalleComposicionMusical detalle : listaDetalleComposicionMusical) 
				{ 
					if (detalle.getId() != null)  // Si trae id significa que esta almacenado 
					{ 
						// Se busca el detalle por el atributo cadenaListaSubDetalles  
						//DetalleComposicionMusical detalleBuscado = (DetalleComposicionMusical) sesion.createQuery("from DetalleComposicionMusical where cadenaListaSubDetalles = '"+detalle.getCadenaListaSubDetalles()+"' and cabeceraComposicionMusical.id = "+cabeceraComposicionMusical.getId()).getSingleResult(); 

						DetalleComposicionMusical detalleBuscado = (DetalleComposicionMusical) sesion.createQuery("from DetalleComposicionMusical where id = " + detalle.getId()).getSingleResult(); 

						detalleBuscado.setOrden(detalle.getOrden()); 
						detalleBuscado.setCadenaListaSubDetalles(detalle.getCadenaListaSubDetalles()); 
						sesion.update(detalleBuscado); 
					} 
					else   // Si no esta guardado el detalle recorrido, se agrega 
					{ 
						detalle.setCabeceraComposicionMusical(cabeceraComposicionMusical); 
						sesion.save(detalle);
 					} 
				} 
			} 
			else  // Si no se recibieron detalles de la clase DetalleComposicionMusical 
			{ 
				sesion.createQuery("delete from DetalleComposicionMusical where cabeceraComposicionMusical.id = "+cabeceraComposicionMusical.getId()).executeUpdate(); 
			} 

			tx.commit(); 
			sesion.close(); 
		} 
		catch (Exception ex) { 
			tx.rollback(); 
			throw new RuntimeException("No se pudo actualizar el registro. " + ex.getMessage()); 
		} 
	} 
	*/

	@Override
	@Transactional 
	public Boolean borrar(int id) 
	{ 
		try { 
			// Se borran las referencias de la tabla DetalleComposicionMusical 
			repositorio_DetalleComposicionMusical.deleteByCabeceraComposicionMusicalId(id); 

			repositorio.deleteById(id); 
			return true; 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo borrar el CabeceraComposicionMusical con el id " + id + ". " + ex.getMessage()); 
		} 
	} 
	
	/*
	@Override
	public Boolean borrar(int id) 
	{ 
		Session sesion = null; 
		Transaction tx = null; 

		try { 
			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 

			// Se borran las referencias de la tabla DetalleComposicionMusical 
			sesion.createQuery("delete from DetalleComposicionMusical where cabeceraComposicionMusical.id = " + id).executeUpdate(); 

			sesion.createQuery("delete from CabeceraComposicionMusical where id = " + id).executeUpdate(); 
			tx.commit(); 
			sesion.close(); 
			return true; 
		} 
		catch (Exception ex) 
		{ 
			tx.rollback(); 
			throw new RuntimeException("No se pudo borrar el CabeceraComposicionMusical con el id " + id + ". " + ex.getMessage()); 
		} 
	} 
	*/


	@Override
	public void procesarDatosExcel(List<CabeceraComposicionMusical> elementosInsertados, List<CabeceraComposicionMusical> elementosActualizados) 
	{ 
		Session sesion = null; 
		Transaction tx = null; 

		try { 
			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 

			for(CabeceraComposicionMusical cabeceraComposicionMusical : elementosInsertados) {  sesion.save(cabeceraComposicionMusical);  } 
			for(CabeceraComposicionMusical cabeceraComposicionMusical : elementosActualizados) {  sesion.update(cabeceraComposicionMusical);  } 

			tx.commit(); 
			sesion.close(); 
		} 
		catch (Exception ex) { 
			tx.rollback(); 
			throw new RuntimeException("Error en el metodo procesarDatosExcel. " + ex.getMessage()); 
		} 
	} 


	@Override
	public void validarCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception 
	{ 
		String mensaje = ""; 
		List<DetalleComposicionMusical> listaDetalleComposicionMusical = cabeceraComposicionMusical.getListaDetalleComposicionMusical(); 

		// Se validan los atributos de la entidad 

		if (cabeceraComposicionMusical.getVisible() == null) mensaje = "El campo visible no posee un valor"; 
		if (! StringUtils.hasText(cabeceraComposicionMusical.getAutor())) mensaje = "El campo autor no posee un valor"; 
		if (! StringUtils.hasText(cabeceraComposicionMusical.getTitulo())) mensaje = "El campo titulo no posee un valor"; 
		if (cabeceraComposicionMusical.getUsuario() == null || cabeceraComposicionMusical.getUsuario().getId() == null) { 
			mensaje = "El campo de tipo Usuario no está seleccionado"; 
		} 

		if (mensaje != "") throw new IllegalArgumentException(mensaje); 

		// Se validan las referencias cruzadas de la entidad DetalleComposicionMusical 

		listaDetalleComposicionMusical.forEach(c-> 
		{ 
			if (c.getOrden() == null) throw new IllegalArgumentException("Un detalle de tipo DetalleComposicionMusical tiene vacío el campo orden"); 
			if (! StringUtils.hasText(c.getCadenaListaSubDetalles())) throw new IllegalArgumentException("Un detalle de tipo DetalleComposicionMusical tiene vacío el campo cadenaListaSubDetalles"); 
		}); 

		/* 
		Session sesion = null; 
		Transaction tx = null; 

		try 
		{ 

			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 

			if(sesion.createQuery("select c from CabeceraComposicionMusical c where c.usuario.id="+cabeceraComposicionMusical.getUsuario().getId()+" and c.id <> "+cabeceraComposicionMusical.getId()).uniqueResult() != null) 
			{ 
				throw new Exception("Ya existen los datos ingresados"); 
			} 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException(ex.getMessage()); 
		} 
		*/ 
	} 


} 




