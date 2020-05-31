package com.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.model.Solicitud;

public interface ISolicitudesService {
	List<Solicitud> buscarTodas();
	Page<Solicitud> buscarTodas(Pageable page);
	Solicitud buscarPorId(Integer idSolicitud);
	void guardar(Solicitud solicitud);
	void eliminar(Integer idSolicitud);
}
