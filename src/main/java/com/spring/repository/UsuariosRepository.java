package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	
	//where Username = ?
	public Usuario findByUsername(String username);

}
