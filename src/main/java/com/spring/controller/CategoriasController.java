package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.model.Categoria;
import com.spring.service.ICategoriasService;

@Controller	
@RequestMapping(value = "/categorias")
public class CategoriasController {
	
	@Autowired
	private ICategoriasService serviceCategorias;

	/**
	 * Metodo que direcciona a la pagina principal (SIN paginacion)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String mostrarIndex(Model model) {
		List<Categoria> listaCategorias = serviceCategorias.buscarTodas();
		model.addAttribute("categorias", listaCategorias);
		return "categorias/listCategorias";
	}
	
	/**
	 * Metodo que direcciona a la pagina principal (CON paginacion)
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/indexPaginate", method = RequestMethod.GET)
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Categoria> listaCategorias = serviceCategorias.buscarTodas(page);
		model.addAttribute("categorias", listaCategorias);
		return "categorias/listCategorias";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String crear(Categoria cetegoria) {
		return "categorias/formCategoria";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idCategoria, Model model) {
		Categoria categoria = serviceCategorias.buscarPorId(idCategoria);
		model.addAttribute("categoria", categoria);
		return "categorias/formCategoria";
	}

	@PostMapping("/save")
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return "categorias/formCategorias";
		}
		
		serviceCategorias.guardar(categoria);
		attributes.addFlashAttribute("mensaje", "El registro se guardo correctamente");
		
		return "redirect:/categorias/index";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idCategoria, RedirectAttributes attributes) {
		serviceCategorias.eliminar(idCategoria);
		attributes.addFlashAttribute("mensaje", "El registro se elimino correctamente");
		
		return "redirect:/categorias/index";
	}

}
