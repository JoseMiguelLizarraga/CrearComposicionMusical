package com.proyecto.service;

import java.util.HashMap;
import java.util.List;
import com.proyecto.model.entity.CabeceraComposicionMusical;
import com.proyecto.model.entity.Usuario;


public interface ICabeceraComposicionMusicalService 
{
	public Usuario obtenerUsuarioLogueado();
	
	public Object obtenerListasBuscador();

	public CabeceraComposicionMusical buscarPorId(int id);

//	public <T> Object llenarSelect2(
//		Class<T> claseEntidad, String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina
//	);

	public List<CabeceraComposicionMusical> listar();

	public HashMap<String, Object> llenarDataTableCabeceraComposicionMusical(
		CabeceraComposicionMusical cabeceraComposicionMusical, int inicio, int registrosPorPagina
	);

	public CabeceraComposicionMusical guardar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception;

	public void actualizar(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception;

	public Boolean borrar(int id);

	public void procesarDatosExcel(List<CabeceraComposicionMusical> elementosInsertados, List<CabeceraComposicionMusical> elementosActualizados);

	public void validarCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical) throws Exception;

}
