package com.project.app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Document {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String type;
	private String path;
	
	@ManyToOne
    @JoinColumn(name = "cour_id")
    private Cour cour;
	
	@ManyToOne
	@JoinColumn(name="enseignant_id")
	private Enseignant enseignant;
	
	public Document() {
    }

    public Document(String name, String type, String path, Cour cour, Enseignant enseignant) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.cour = cour;
        this.enseignant = enseignant;
    }

}
