package com.project.app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class DevoirRendu {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDevoirRendu;
	 @Lob
	 private byte[] pdf;
	 @ManyToOne
	 @JoinColumn(name = "id_devoir", nullable = false)
	 private Devoir devoir;
	 
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name="etudiant_id")
	 private Etudiant etudiant;

}
