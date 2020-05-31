package com.spring.tools;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Herramientas {
	
	/**
	 * Metodo para guardar imagenes en la ruta indicada
	 * @param multiPart
	 * @param ruta
	 * @return
	 */
	public static String guardarArchivo(MultipartFile multiPart, String ruta) {
		// Obtener el nombre original del archivo
		String nombreOriginal = multiPart.getOriginalFilename();
		// Quitar los epacios
		nombreOriginal = nombreOriginal.replace(" ", "_");
		// Añadir caracteres aleatorios para evitar nombre duplicados
		String nombreFinal = randomAlphaNumeric(4) + nombreOriginal;
		
		try {
			// Formar el nombre del fichero para guardarlo en el disco duro
			File imageFile = new File(ruta + nombreFinal);
			// Guardar fisicamente el archivo en el disco duro
			multiPart.transferTo(imageFile);
			
			return nombreFinal;
			
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Metodo para generar una cadena aleatoria de longitud N
	 * @param count
	 * @return
	 */
	public static String randomAlphaNumeric(int longitud) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder cadena = new StringBuilder();
		
		while (longitud-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			cadena.append(CARACTERES.charAt(character));
		}
		
		return cadena.toString();
	}


}
