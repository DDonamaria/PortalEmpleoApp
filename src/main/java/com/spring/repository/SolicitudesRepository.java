package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer>{
	
}
