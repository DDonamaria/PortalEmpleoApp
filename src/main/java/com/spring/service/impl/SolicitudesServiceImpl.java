package com.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.model.Solicitud;
import com.spring.repository.SolicitudesRepository;
import com.spring.service.ISolicitudesService;

@Service
public class SolicitudesServiceImpl implements ISolicitudesService {
	
	@Autowired
	private SolicitudesRepository repoSolicitudes;

	@Override
	public List<Solicitud> buscarTodas() {
		return repoSolicitudes.findAll();
	}

	@Override
	public Page<Solicitud> buscarTodas(Pageable page) {
		return repoSolicitudes.findAll(page);
	}

	@Override
	public Solicitud buscarPorId(Integer idSolicitud) {
		Optional<Solicitud> optional = repoSolicitudes.findById(idSolicitud);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}

	@Override
	public void guardar(Solicitud solicitud) {
		repoSolicitudes.save(solicitud);
	}

	@Override
	public void eliminar(Integer idSolicitud) {
		repoSolicitudes.deleteById(idSolicitud);
	}

}
