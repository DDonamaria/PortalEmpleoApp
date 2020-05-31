package com.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.model.Categoria;

public interface ICategoriasService {
	List<Categoria> buscarTodas();
	Page<Categoria> buscarTodas(Pageable page);
	Categoria buscarPorId(Integer idCategoria);
	void guardar(Categoria categoria);
	void eliminar(Integer idCategoria);

}
