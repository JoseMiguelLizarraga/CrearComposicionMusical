package com.proyecto.model.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table; 


@Entity 
@Table(name="parametros_login") 
public class ParametrosLogin 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 
	
	@Column(name = "duracion_token_milisegundos") 
	private int duracionTokenMilisegundos; 
	
	@Column(name = "mensaje_error_autenticacion") 
	private String mensajeErrorAutenticacion; 
	
	@Column(name = "mensaje_token_expirado") 
	private String mensajeTokenExpirado; 
	
	@Column(name = "mensaje_usuario_deshabilitado") 
	private String mensajeUsuarioDeshabilitado; 
	
	@Column(name = "mensaje_error_interno") 
	private String mensajeErrorInterno;

	
	public ParametrosLogin() {
	
	}

	public ParametrosLogin(Integer id, int duracionTokenMilisegundos, String mensajeErrorAutenticacion, String mensajeTokenExpirado, String mensajeErrorInterno) 
	{
		this.id = id;
		this.duracionTokenMilisegundos = duracionTokenMilisegundos;
		this.mensajeErrorAutenticacion = mensajeErrorAutenticacion;
		this.mensajeTokenExpirado = mensajeTokenExpirado;
		this.mensajeErrorInterno = mensajeErrorInterno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDuracionTokenMilisegundos() {
		return duracionTokenMilisegundos;
	}

	public void setDuracionTokenMilisegundos(int duracionTokenMilisegundos) {
		this.duracionTokenMilisegundos = duracionTokenMilisegundos;
	}

	public String getMensajeErrorAutenticacion() {
		return mensajeErrorAutenticacion;
	}

	public void setMensajeErrorAutenticacion(String mensajeErrorAutenticacion) {
		this.mensajeErrorAutenticacion = mensajeErrorAutenticacion;
	}

	public String getMensajeTokenExpirado() {
		return mensajeTokenExpirado;
	}

	public void setMensajeTokenExpirado(String mensajeTokenExpirado) {
		this.mensajeTokenExpirado = mensajeTokenExpirado;
	}

	public String getMensajeUsuarioDeshabilitado() {
		return mensajeUsuarioDeshabilitado;
	}

	public void setMensajeUsuarioDeshabilitado(String mensajeUsuarioDeshabilitado) {
		this.mensajeUsuarioDeshabilitado = mensajeUsuarioDeshabilitado;
	}

	public String getMensajeErrorInterno() {
		return mensajeErrorInterno;
	}

	public void setMensajeErrorInterno(String mensajeErrorInterno) {
		this.mensajeErrorInterno = mensajeErrorInterno;
	} 

} 

