package com.spring.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.model.Categoria;
import com.spring.model.Perfil;
import com.spring.model.Usuario;
import com.spring.model.Vacante;
import com.spring.service.ICategoriasService;
import com.spring.service.IUsuariosService;
import com.spring.service.IVacantesService;

@Controller
public class HomeController {
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String mostrarHome(Model model) {
		return "home";
	}
	
	/**
	 * Metodo que nos lleva al inicio tras iniciar sesion y carga el usuario en sesion
	 * @param auth
	 * @param session
	 * @return
	 */
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		
		String username = auth.getName();
		System.out.println("El usuario " + username + " ha iniciado sesion");
		
		//Añadimos el usuario a la session
		if(session.getAttribute("uasuario") == null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			usuario.setPassword(null); //eliminamos la contraseña para que no tener este dato en session
			System.out.println("Usuario -> " + usuario);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/";
	}
	
	/**
	 * Metodo para filtrar las vacantes en la vista princilpal
	 * @param vacante
	 * @param model
	 * @return
	 */
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		
		// where descripcion like '%?%' - La descripcion no tiene por que coincidir exactamente
		ExampleMatcher martcher = ExampleMatcher.matching().withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		
		Example<Vacante> example = Example.of(vacante, martcher);
		List<Vacante> listaBusqueda = serviceVacantes.buscarByExample(example);
		
		model.addAttribute("vacantes", listaBusqueda);
		
		return "home";
	}
	
	@GetMapping("/detail/{id}")
	public String verDetalles(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		return "vacantes/detalle";
	}
	
	/**
	 * Metodo para direccionar al formulario de registro
	 * @param usuario
	 * @return
	 */
	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardarUsuario(Usuario usuario, RedirectAttributes attributes) {
		
		String pwdEncriptado = passwordEncoder.encode(usuario.getPassword());
		
		usuario.setPassword(pwdEncriptado);
		usuario.setEstatus(1); //Estatus por defecto activado
		usuario.setFechaRegistro(new Date());
		
		// Perfil que le asignaremos al nuevo usuario
		Perfil perfil = new Perfil();
		perfil.setId(3); // Por defecto perfil tipo USUARIO
		usuario.agregarPerfil(perfil);
		
		//Guardamos el usuario y el perfil se guarda automaticamente
		serviceUsuarios.guardar(usuario);
				
		attributes.addFlashAttribute("mensaje", "El registro se guardo correctamente!");
		
		return "redirect:/usuarios/index";
	}
	
	/**
	 * Metodo para direccionar al formulario de inicio de sesion
	 * @return
	 */
	@GetMapping("/login" )
	public String mostrarLogin() {
		return "formLogin";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request){
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		
		return "redirect:/";
	}
	
	/**
	 * Metodo que durante el data binding pone null en los String que esten vacios
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//Editamos la clase String y aplicamos el metodo encargado de pone null cuando el atributo este vacio
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	/**
	 * Metodo para crear model comun para toda la clase
	 * @param model
	 */
	@ModelAttribute
	public void setModelComun(Model model) {
		List<Vacante> vacantesDest = serviceVacantes.buscarDestacadas();
		model.addAttribute("vacantes", vacantesDest);
		
		List<Categoria> categoriasDisp = serviceCategorias.buscarTodas();
		model.addAttribute("categorias", categoriasDisp); 
		
		//Objeto que mandamos a la vista y donde guardaremos la informacion de la busqueda
		Vacante vacanteSearch = new Vacante(); 
		vacanteSearch.reset(); //Limpia la imagen por defecto ya que la busqueda no se realiza por imagen
		model.addAttribute("search", vacanteSearch);
	}
	
}
