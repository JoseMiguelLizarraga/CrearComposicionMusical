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
import org.hibernate.annotations.Fetch; 
import org.hibernate.annotations.FetchMode; 


@Entity 
@Table(name="usuario") 
public class Usuario 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "username") 
	private String username; 

	@Column(name = "password") 
	private String password; 

	@Column(name = "largo_password") 
	private Integer largoPassword; 

	@Column(name = "nombre") 
	private String nombre; 

	@Column(name = "apellido_paterno") 
	private String apellidoPaterno; 

	@Column(name = "apellido_materno") 
	private String apellidoMaterno; 

	@Column(name = "rut") 
	private String rut; 

	@Column(name = "telefono") 
	private String telefono; 

	@Column(name = "activo") 
	private Boolean activo; 

	@Column(name = "visible") 
	private Boolean visible; 

	@JsonIgnoreProperties({"usuario"}) 
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY) 
	@Fetch(value = FetchMode.SUBSELECT) 
	private List<UsuarioCargo> listaUsuarioCargo = new ArrayList<>(); 


	public Usuario() { 

	} 
	
	
	public Usuario(Integer id) {
		this.id = id;
	}


	public Usuario(Integer id, String username, String password, Integer largoPassword, String nombre, String apellidoPaterno, String apellidoMaterno, String rut, String telefono, Boolean activo, Boolean visible) 
	{ 
		this.id = id; 
		this.username = username; 
		this.password = password; 
		this.largoPassword = largoPassword; 
		this.nombre = nombre; 
		this.apellidoPaterno = apellidoPaterno; 
		this.apellidoMaterno = apellidoMaterno; 
		this.rut = rut; 
		this.telefono = telefono; 
		this.activo = activo; 
		this.visible = visible; 
	} 

	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public String getUsername() { 
		return this.username; 
	}  
	public void setUsername(String username) { 
		this.username = username; 
	}  
	public String getPassword() { 
		return this.password; 
	}  
	public void setPassword(String password) { 
		this.password = password; 
	}  
	public Integer getLargoPassword() { 
		return this.largoPassword; 
	}  
	public void setLargoPassword(Integer largoPassword) { 
		this.largoPassword = largoPassword; 
	}  
	public String getNombre() { 
		return this.nombre; 
	}  
	public void setNombre(String nombre) { 
		this.nombre = nombre; 
	}  
	public String getApellidoPaterno() { 
		return this.apellidoPaterno; 
	}  
	public void setApellidoPaterno(String apellidoPaterno) { 
		this.apellidoPaterno = apellidoPaterno; 
	}  
	public String getApellidoMaterno() { 
		return this.apellidoMaterno; 
	}  
	public void setApellidoMaterno(String apellidoMaterno) { 
		this.apellidoMaterno = apellidoMaterno; 
	}  
	public String getRut() { 
		return this.rut; 
	}  
	public void setRut(String rut) { 
		this.rut = rut; 
	}  
	public String getTelefono() { 
		return this.telefono; 
	}  
	public void setTelefono(String telefono) { 
		this.telefono = telefono; 
	}  
	public Boolean getActivo() { 
		return this.activo; 
	}  
	public void setActivo(Boolean activo) { 
		this.activo = activo; 
	}  
	public Boolean getVisible() { 
		return this.visible; 
	}  
	public void setVisible(Boolean visible) { 
		this.visible = visible; 
	}  
	public List<UsuarioCargo> getListaUsuarioCargo() { 
		return this.listaUsuarioCargo; 
	}  
	public void setListaUsuarioCargo(List<UsuarioCargo> listaUsuarioCargo) { 
		this.listaUsuarioCargo = listaUsuarioCargo; 
	}  

} 

