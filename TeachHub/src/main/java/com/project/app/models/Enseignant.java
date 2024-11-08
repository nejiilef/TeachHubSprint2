package com.project.app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Enseignant {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	
	private Long id;
	private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String specialite;

}
