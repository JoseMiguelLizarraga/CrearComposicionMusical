package com.proyecto.model.entity; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.Column; 
import javax.persistence.FetchType; 
import javax.persistence.Table; 
import java.util.List; 
import java.util.ArrayList; 
import javax.persistence.OneToMany; 
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 
import javax.persistence.ManyToOne; 
import javax.persistence.JoinColumn; 


@Entity 
@Table(name="cabecera_composicion_musical") 
public class CabeceraComposicionMusical 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "visible") 
	private Boolean visible; 

	@Column(name = "autor") 
	private String autor; 

	@Column(name = "titulo") 
	private String titulo; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase Usuario 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "usuario_id") 
	private Usuario usuario; 

	@JsonIgnoreProperties({"cabeceraComposicionMusical"}) 
	@OneToMany(mappedBy="cabeceraComposicionMusical", fetch=FetchType.LAZY) 
	private List<DetalleComposicionMusical> listaDetalleComposicionMusical = new ArrayList<>(); 

	public CabeceraComposicionMusical() 
	{ 

	} 

	public CabeceraComposicionMusical(Integer id, Usuario usuario, Boolean visible, String autor, String titulo) 
	{ 
		this.id = id;
		this.usuario = usuario; 
		this.visible = visible; 
		this.autor = autor; 
		this.titulo = titulo; 
	} 

	public Boolean getVisible() { 
		return this.visible; 
	}  
	public void setVisible(Boolean visible) { 
		this.visible = visible; 
	}  
	public String getAutor() { 
		return this.autor; 
	}  
	public void setAutor(String autor) { 
		this.autor = autor; 
	}  
	public String getTitulo() { 
		return this.titulo; 
	}  
	public void setTitulo(String titulo) { 
		this.titulo = titulo; 
	}  
	
	public Usuario getUsuario() { 
		return this.usuario; 
	}  
	
//	public Usuario getUsuario() 
//	{ 
//		Usuario usuario = new Usuario(this.usuario.getId());
//		usuario.setNombre(this.usuario.getNombre());
//		return usuario;  // Asi no muestra datos personales del usuario
//	}  
	
	public void setUsuario(Usuario usuario) { 
		this.usuario = usuario; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public List<DetalleComposicionMusical> getListaDetalleComposicionMusical() { 
		return this.listaDetalleComposicionMusical; 
	}  
	public void setListaDetalleComposicionMusical(List<DetalleComposicionMusical> listaDetalleComposicionMusical) { 
		this.listaDetalleComposicionMusical = listaDetalleComposicionMusical; 
	}  
} 

