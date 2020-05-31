package com.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.model.Solicitud;
import com.spring.model.Usuario;
import com.spring.model.Vacante;
import com.spring.service.ISolicitudesService;
import com.spring.service.IUsuariosService;
import com.spring.service.IVacantesService;
import com.spring.tools.Herramientas;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
	
	@Value("${portalempleo.solicitudes}")
	private String rutaSolicitudes;
	
	// Inyectamos una instancia desde nuestro ApplicationContext
    @Autowired
	private ISolicitudesService serviceSolicitudes;

    // Inyectamos una instancia desde nuestro ApplicationContext
    @Autowired
	private IVacantesService serviceVacantes;
    
    @Autowired
   	private IUsuariosService serviceUsuarios;
    
    /**
     * Metodo para mostrar listado de Solicitudes (SIN paginacion)
     * @param model
     * @return
     */
    @GetMapping("/index")
	public String mostrarIndex(Model model) {
    	List<Solicitud> lista = serviceSolicitudes.buscarTodas();
    	model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";
	}
    
    /**
     * Metodo para mostrar listado de Solicitudes (CON paginacion)
     * @param model
     * @param page
     * @return
     */
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Solicitud> lista = serviceSolicitudes.buscarTodas(page);
		model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";
	}
	
	/**
	 * Metodo para direccionar al formulario de aplicar
	 * @param solicitud
	 * @param idVacante
	 * @param model
	 * @return
	 */
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable Integer idVacante, Model model) {
		// Obtenemos Vacante seleccionada para mostrarla en la vista
		Vacante vacanteSeleccionada = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacanteSeleccionada);
		
		return "solicitudes/formSolicitud";
	}
	
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, Model model, HttpSession session,
			@RequestParam("archivoCV") MultipartFile multiPart, RedirectAttributes attributes, Authentication authentication) {	
		
		//Username que inicio sesion
		String username = authentication.getName();
		
		// Si ha habido errores de databinding mostrar formulario
		if (result.hasErrors()){
			return "solicitudes/formSolicitud";
		}	
		
		//Si se ha cargado un fichero lo guardamos y añadimos al objeto
		if (!multiPart.isEmpty()) {
			String nombreArchivo = Herramientas.guardarArchivo(multiPart, rutaSolicitudes);
			
			if (nombreArchivo!=null){		
				solicitud.setArchivo(nombreArchivo); 
			}	
		}

		Usuario usuario = serviceUsuarios.buscarPorUsername(username);	
		
		solicitud.setUsuario(usuario);
		solicitud.setFecha(new Date());
		
		serviceSolicitudes.guardar(solicitud);
		
		attributes.addFlashAttribute("mensaje", "La solicitud se realizo correctamente");
			
		return "redirect:/";		
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes) {
		serviceSolicitudes.eliminar(idSolicitud);
		attributes.addFlashAttribute("mensaje", "La solicitud se elimino correctamente");
		
		return "redirect:/solicitudes/indexPaginate";
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
