package com.spring.service;

import java.util.List;

import com.spring.model.Usuario;

public interface IUsuariosService {
	
	List<Usuario> buscarTodos();
	Usuario buscarPorId(Integer idUsuario);
	Usuario buscarPorUsername(String username);
	void guardar(Usuario usuario);
	void eliminar(Integer idUsuario);

}
