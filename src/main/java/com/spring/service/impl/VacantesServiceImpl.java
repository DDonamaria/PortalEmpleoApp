package com.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.model.Vacante;
import com.spring.repository.VacantesRepository;
import com.spring.service.IVacantesService;

@Service
public class VacantesServiceImpl implements IVacantesService{
	
	@Autowired
	private VacantesRepository repoVacantes;

	@Override
	public List<Vacante> buscarTodas() {
		return repoVacantes.findAll();
	}
	
	@Override
	public Page<Vacante> buscarTodas(Pageable page) {
		return repoVacantes.findAll(page);
	}
	
	@Override
	public List<Vacante> buscarDestacadas() {
		return repoVacantes.findByDestacadaAndEstatusOrderByIdDesc(1, "Aprobada");
	}
	
	/**
	 * Buscar un registro mediante un objeto 
	 */
	@Override
	public List<Vacante> buscarByExample(Example<Vacante> example) {
		return repoVacantes.findAll(example);
	}

	@Override
	public Vacante buscarPorId(Integer idVacante) {
		Optional<Vacante> optional = repoVacantes.findById(idVacante);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(Vacante vacante) {
		repoVacantes.save(vacante);
	}

	@Override
	public void eliminar(Integer idVacante) {
		repoVacantes.deleteById(idVacante);
	}

}
