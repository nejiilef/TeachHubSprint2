package com.project.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SousGroupe {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSousGroupe;
    
    private String nom;
    
    @ManyToOne
    @JoinColumn(name = "cour_id")
    private Cour cour;
    
    @ManyToMany
    @JoinTable(name = "sousgroupe_etudiant",
               joinColumns = @JoinColumn(name = "sousgroupe_id"),
               inverseJoinColumns = @JoinColumn(name = "etudiant_id"))
    private List<Etudiant> etudiants = new ArrayList<>();
    
    
    
    
    public SousGroupe() {
        super();
    }
    
    public SousGroupe(String nom, Cour cour) {
        this.nom = nom;
        this.cour = cour;
    }
}
