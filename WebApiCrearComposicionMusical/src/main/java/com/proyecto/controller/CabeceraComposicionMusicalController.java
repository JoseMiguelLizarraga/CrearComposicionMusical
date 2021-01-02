package com.proyecto.controller; 

import com.proyecto.model.dao.DaoGenerico; 
import com.proyecto.model.dao.CabeceraComposicionMusicalDao; 
import com.proyecto.model.entity.CabeceraComposicionMusical; 
import com.proyecto.model.entity.Usuario; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import javax.servlet.http.HttpServletRequest; 
import org.springframework.web.bind.annotation.*; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.validation.BindingResult; 

// PDF 
import com.itextpdf.text.Document; 
import com.itextpdf.text.DocumentException; 
import com.itextpdf.text.Phrase; 
import com.itextpdf.text.pdf.PdfPCell; 
import com.itextpdf.text.pdf.PdfPTable; 
import com.itextpdf.text.pdf.PdfWriter; 

// Otras importaciones necesarias para trabajar con archivos 
import org.springframework.core.io.InputStreamResource; 
import java.io.ByteArrayOutputStream; 
import java.io.ByteArrayInputStream; 
import org.springframework.http.HttpHeaders; 
import org.springframework.http.MediaType; 


@CrossOrigin 
@RestController 
@RequestMapping("/api/CabeceraComposicionMusical") 
public class CabeceraComposicionMusicalController 
{ 
	@Autowired private CabeceraComposicionMusicalDao dao; 
	@Autowired private DaoGenerico daoGenerico; 

	@GetMapping 
	public Object listar()  // url:    /CabeceraComposicionMusical/ 
	{ 
		try { 
			return dao.listar(); 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	@GetMapping(path = {"/obtenerListasBuscador"}) 
	public Object obtenerListasBuscador()  // url:    /CabeceraComposicionMusical/obtenerListasBuscador 
	{ 
		try { 
			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("listaUsuario", daoGenerico.findAll("select new map(c.id as id, c.apellidoPaterno as apellidoPaterno, c.visible as visible, c.nombre as nombre, c.activo as activo, c.largoPassword as largoPassword, c.telefono as telefono, c.password as password, c.rut as rut, c.username as username, c.apellidoMaterno as apellidoMaterno) from Usuario c")); 

			return mapa; 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	@RequestMapping(value = "/llenarSelect2") 
	public @ResponseBody Object llenarSelect2(String clase, String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		try { 
			if (clase.equals("Usuario")) { 
				return dao.llenarSelect2(Usuario.class, atributoBuscado, busqueda, registrosPorPagina, numeroPagina); 
			} 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 

		return null; 
	} 


	private Map<String, Object> llenarInformacionDataTable(CabeceraComposicionMusical cabeceraComposicionMusical, HttpServletRequest request) 
	{ 
		try { 
			int inicio = Integer.parseInt(request.getParameter("start")); 
			int registrosPorPagina = Integer.parseInt(request.getParameter("length"));  // Es la cantidad de registros por pagina  


			Map<String, Object> mapa = dao.llenarDataTableCabeceraComposicionMusical(cabeceraComposicionMusical, inicio, registrosPorPagina); 
			return mapa; 
		} 
		catch(Exception ex) 
		{ 
			throw new IllegalArgumentException(ex.getMessage()); 
		} 
	} 


	@RequestMapping(path = "/llenarDataTable") 
	public @ResponseBody Object llenarDataTable(CabeceraComposicionMusical cabeceraComposicionMusical, HttpServletRequest request) 
	{ 
		try { 
			Map<String, Object> mapa = llenarInformacionDataTable(cabeceraComposicionMusical, request); 
			return mapa; 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@PostMapping 
	public ResponseEntity guardar(@RequestBody CabeceraComposicionMusical cabeceraComposicionMusical, BindingResult result) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			return ResponseEntity.ok(dao.guardar(cabeceraComposicionMusical)); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@GetMapping(path = {"/{id}"})  // url:    /CabeceraComposicionMusical/1  
	public Object editar(@PathVariable("id") int id) 
	{ 
		try { 
			return dao.buscarPorId(id);  
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	@PutMapping 
	@ResponseBody 
	public ResponseEntity editar(@RequestBody CabeceraComposicionMusical cabeceraComposicionMusical, BindingResult result) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			dao.actualizar(cabeceraComposicionMusical); 
			return new ResponseEntity(HttpStatus.OK); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@DeleteMapping(path = "/{id}") 
	public Object borrar(@PathVariable("id") int id) 
	{ 
		try { 
			dao.borrar(id); 
			return new ResponseEntity(HttpStatus.OK); 
		} 
		catch(Exception ex) 
		{ 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@RequestMapping(value = "/generarPDF", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE) 
	public ResponseEntity<InputStreamResource> generarPDF(CabeceraComposicionMusical cabeceraComposicionMusical, HttpServletRequest request) 
	{ 
		try { 
			List<CabeceraComposicionMusical> listaCabeceraComposicionMusical = (List<CabeceraComposicionMusical>) this.llenarInformacionDataTable(cabeceraComposicionMusical, request).get("data"); 

			Document documento = new Document(); 
			ByteArrayOutputStream out = new ByteArrayOutputStream(); 

			PdfPTable tabla = new PdfPTable(5);  // Se le pasa como parametro el numero de columnas 
			PdfPCell th; 

			th = new PdfPCell(new Phrase("visible")); 
			tabla.addCell(th); 

			th = new PdfPCell(new Phrase("autor")); 
			tabla.addCell(th); 

			th = new PdfPCell(new Phrase("titulo")); 
			tabla.addCell(th); 

			th = new PdfPCell(new Phrase("usuario")); 
			tabla.addCell(th); 

			th = new PdfPCell(new Phrase("id")); 
			tabla.addCell(th); 

			for(CabeceraComposicionMusical c : listaCabeceraComposicionMusical) 
			{ 
				PdfPCell td; 
				td = new PdfPCell(new Phrase( c.getVisible() ? "si" : "no" )); 
				tabla.addCell(td); 

				td = new PdfPCell(new Phrase(c.getAutor())); 
				tabla.addCell(td); 

				td = new PdfPCell(new Phrase(c.getTitulo())); 
				tabla.addCell(td); 

				td = new PdfPCell(new Phrase(String.valueOf(c.getUsuario().getId()))); 
				tabla.addCell(td); 

				td = new PdfPCell(new Phrase(c.getId())); 
				tabla.addCell(td); 

			} 

			PdfWriter.getInstance(documento, out); 
			documento.open(); 
			documento.add(tabla); 

			documento.close(); 

			ByteArrayInputStream salida = new ByteArrayInputStream(out.toByteArray()); 

			HttpHeaders headers = new HttpHeaders(); 
			headers.add("Content-Disposition", "inline; filename=reporteCabeceraComposicionMusical.pdf"); 

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF) 
				.body(new InputStreamResource(salida)); 
		} 
		catch(DocumentException ex) 
		{ 
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR); 
		} 
		catch(Exception ex) 
		{ 
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR); 
		} 
	} 


} 



