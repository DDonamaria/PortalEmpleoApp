package com.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.model.Categoria;
import com.spring.repository.CategoriasRepository;
import com.spring.service.ICategoriasService;

@Service
public class CategoriasServiceImpl implements ICategoriasService {
	
	@Autowired
	private CategoriasRepository repoCategorias;

	@Override
	public List<Categoria> buscarTodas() {
		return repoCategorias.findAll();
	}
	
	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		return repoCategorias.findAll(page);
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		Optional<Categoria> optional = repoCategorias.findById(idCategoria);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(Categoria categoria) {
		repoCategorias.save(categoria);
	}

	@Override
	public void eliminar(Integer idCategoria) {
		repoCategorias.deleteById(idCategoria);
	}

}
