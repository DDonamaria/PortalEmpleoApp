package com.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.model.Usuario;
import com.spring.repository.UsuariosRepository;
import com.spring.service.IUsuariosService;

@Service
public class UsuariosServiceImpl implements IUsuariosService {
	
	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Override
	public List<Usuario> buscarTodos() {
		return repoUsuarios.findAll();
	}

	@Override
	public Usuario buscarPorId(Integer idUsuario) {
		Optional<Usuario> optional = repoUsuarios.findById(idUsuario);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	@Override
	public Usuario buscarPorUsername(String username) {
		return repoUsuarios.findByUsername(username);
	}

	@Override
	public void guardar(Usuario usuario) {
		repoUsuarios.save(usuario);
	}

	@Override
	public void eliminar(Integer idUsuario) {
		repoUsuarios.deleteById(idUsuario);
	}

}
