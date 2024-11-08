package com.project.app.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
	
	private String nom;
	private String prenom;
	private String email;
	private String MotDePasse;
	private String role;

}
