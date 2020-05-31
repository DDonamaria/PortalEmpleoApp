package com.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${portalempleo.imagenes}")
	private String rutaImagenes;
	
	@Value("${portalempleo.solicitudes}")
	private String rutaSolicitudes;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		registry.addResourceHandler("/logotipos/**").addResourceLocations("file:"+rutaImagenes);
		
		registry.addResourceHandler("/curriculums/**").addResourceLocations("file:"+rutaSolicitudes);
		
	}

}
