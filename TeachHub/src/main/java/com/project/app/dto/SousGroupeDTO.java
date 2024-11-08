package com.project.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class SousGroupeDTO {
	
	private Integer idSousGroupe;
    
    private String nom;

    private List<String> etudiants;
    
}
