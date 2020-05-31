package com.spring.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	//Bean datasorce configurado en application.properties
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("select username, password, estatus from Usuarios where username=?")
			.authoritiesByUsernameQuery("select u.username, p.perfil from UsuarioPerfil up " +
			"inner join Usuarios u on u.id = up.idUsuario " +
			"inner join Perfiles p on p.id = up.idPerfil " +
			"where u.username = ?");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		
		// Recursos estáticos que no requieren autenticación
		.antMatchers(
			"/bcrypt/**",
			"/bootstrap/**",
			"/images/**",
			"/tinymce/**",
			"/logotipos/**").permitAll()
		
		// Vistas públicas que no requieren autenticación
		.antMatchers("/",
			"/signup",
			"/search",
			"/detail/**").permitAll()
		
		// Asignacion de permisos a URLs por ROLES
		.antMatchers("/vacantes/**").hasAnyAuthority("EDITOR","ADMINISTRADOR")
		.antMatchers("/categorias/**").hasAnyAuthority("EDITOR","ADMINISTRADOR")
		.antMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")
		.antMatchers("/solicitudes/**").hasAnyAuthority("EDITOR","ADMINISTRADOR")
		.antMatchers("/solicitudes/create/**",
				     "/solicitudes/save/**").hasAuthority("USUARIO")
		
		// Todas las demás URLs de la Aplicación requieren autenticación
		.anyRequest().authenticated()
		
		// El formulario de Login no requiere autenticacion
		.and().formLogin().loginPage("/login").permitAll();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}