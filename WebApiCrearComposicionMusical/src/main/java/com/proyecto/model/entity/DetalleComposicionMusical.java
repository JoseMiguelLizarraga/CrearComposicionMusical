package com.proyecto.model.entity; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.Column; 
import javax.persistence.FetchType; 
import javax.persistence.Table; 
import javax.persistence.ManyToOne; 
import javax.persistence.JoinColumn; 


@Entity 
@Table(name="detalle_composicion_musical") 
public class DetalleComposicionMusical 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "cadenaListaSubDetalles") 
	private String cadenaListaSubDetalles; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase CabeceraComposicionMusical 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "cabecera_composicion_musical_id") 
	private CabeceraComposicionMusical cabeceraComposicionMusical; 

	@Column(name = "orden") 
	private Integer orden; 

	public DetalleComposicionMusical() 
	{ 

	} 

	public DetalleComposicionMusical(String cadenaListaSubDetalles, CabeceraComposicionMusical cabeceraComposicionMusical, Integer orden, Integer id) 
	{ 
		this.cadenaListaSubDetalles = cadenaListaSubDetalles; 
		this.cabeceraComposicionMusical = cabeceraComposicionMusical; 
		this.orden = orden; 
		this.id = id; 
	} 

	public String getCadenaListaSubDetalles() { 
		return this.cadenaListaSubDetalles; 
	}  
	public void setCadenaListaSubDetalles(String cadenaListaSubDetalles) { 
		this.cadenaListaSubDetalles = cadenaListaSubDetalles; 
	}  
	public CabeceraComposicionMusical getCabeceraComposicionMusical() { 
		return this.cabeceraComposicionMusical; 
	}  
	public void setCabeceraComposicionMusical(CabeceraComposicionMusical cabeceraComposicionMusical) { 
		this.cabeceraComposicionMusical = cabeceraComposicionMusical; 
	}  
	public Integer getOrden() { 
		return this.orden; 
	}  
	public void setOrden(Integer orden) { 
		this.orden = orden; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
} 

