package com.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.model.Categoria;
import com.spring.model.Vacante;
import com.spring.service.ICategoriasService;
import com.spring.service.IVacantesService;
import com.spring.tools.Herramientas;

@Controller
@RequestMapping(value="/vacantes")
public class VacantesController {
	
	@Value("${portalempleo.imagenes}")
	private String rutaImagenes;
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	/**
	 * 
	 * Metodo para mostrar listado de Vacantes (SIN paginacion)
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> listaVacantes = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", listaVacantes);
		return "vacantes/listVacantes";
	}
	
	/**
	 * Metodo para mostrar listado de Vacantes (CON paginacion)
	 * @param model
	 * @param page
	 * @return
	 */
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante> listaVacantes = serviceVacantes.buscarTodas(page);
		model.addAttribute("vacantes", listaVacantes);
		return "vacantes/listVacantes";
	}
	
	
	
	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) {
		return "vacantes/formVacante";
	}
	
	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, RedirectAttributes attributes, @RequestParam("archivoImagen") MultipartFile multiPart) {
		// Si ha habido errores de databinding mostrar formulario
		if(result.hasErrors()) {
			return "vacantes/formVacante";
		}
		
		//Si se ha cargado una imagen la guardamos y añadimos al objeto vacante
		if (!multiPart.isEmpty()) {
			String nombreImagen = Herramientas.guardarArchivo(multiPart, rutaImagenes);
			
			if (nombreImagen != null){ 
				vacante.setImagen(nombreImagen);
			}
		}
		
		serviceVacantes.guardar(vacante);
		
		attributes.addFlashAttribute("mensaje", "El registro se guardo correctamente");
		
		return "redirect:/vacantes/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		
		return "vacantes/formVacante";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante, RedirectAttributes attributes) {
		serviceVacantes.eliminar(idVacante);
		attributes.addFlashAttribute("mensaje", "El registro se elimino correctamente");
		
		return "redirect:/vacantes/indexPaginate";
	}
	
	/**
	 * Metodo para crear model comun para toda la clase
	 * @param model
	 */
	@ModelAttribute
	public void setModelComun(Model model) {
		model.addAttribute("estatus", getEstatus());

		List<Categoria> categorias = serviceCategorias.buscarTodas();
		model.addAttribute("categorias", categorias);
	}
	
	public List<String> getEstatus(){
		List<String> listaEstatus = new LinkedList<String>();
		listaEstatus.add("Creada");
		listaEstatus.add("Aprobada");
		listaEstatus.add("Eliminada");
		
		return listaEstatus;
	}
	
	
	/**
	 * Metodo para indicar al Data Binding el formato en que llega la fecha
	 * 
	 * @param binder
	 */
	@InitBinder
	private void dateBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//Creamos el nuevo CustomDateEditor
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    //Registramos el CustomDateEditor como CustomEditor en el tipo de dato "Date"
	    binder.registerCustomEditor(Date.class, editor);
	}

}
