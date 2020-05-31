package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.model.Usuario;
import com.spring.service.IUsuariosService;

@Controller
@RequestMapping(value = "/usuarios")
public class UsuariosController {
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Usuario> usuarios = serviceUsuarios.buscarTodos();
		model.addAttribute("usuarios", usuarios);
		return "usuarios/listUsuarios";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		System.out.println("Usuario en controller: " + idUsuario);
		serviceUsuarios.eliminar(idUsuario);
		
		attributes.addFlashAttribute("mensaje", "El registro se guardo correctamente!");
		
		return "redirect:/usuarios/index";
	}

}
