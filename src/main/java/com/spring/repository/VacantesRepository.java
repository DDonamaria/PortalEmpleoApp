package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.model.Vacante;

public interface VacantesRepository extends JpaRepository<Vacante, Integer> {
	
	//select * from Vacantes where Estatus = ?
	List<Vacante> findByEstatus(String estatus);
	
	List<Vacante> findByDestacadaAndEstatusOrderByIdDesc(int destacada, String estatus);
	
	List<Vacante> findBySalarioBetweenOrderBySalarioDesc(double s1, double s2);
	
	List<Vacante> findByEstatusIn(String[] estatus); 

}
