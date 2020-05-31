package com.spring.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.model.Vacante;

public interface IVacantesService {
	List<Vacante> buscarTodas();
	Page<Vacante> buscarTodas(Pageable page);
	List<Vacante> buscarDestacadas();
	//Busqueda mediante objeto en funcion de la informacion de los campos
	List<Vacante> buscarByExample(Example<Vacante> example);
	Vacante buscarPorId(Integer idVacante);
	void guardar(Vacante vacante);
	void eliminar(Integer idVacante);
	
	

}
