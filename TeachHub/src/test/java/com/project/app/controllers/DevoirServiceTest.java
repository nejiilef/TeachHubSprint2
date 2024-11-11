package com.project.app.controllers;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.project.app.dto.DevoirDTO;
import com.project.app.models.Cour;
import com.project.app.models.Devoir;
import com.project.app.repository.CourRepository;
import com.project.app.repository.DevoirRepository;
import com.project.app.services.jwt.DevoirService;

public class DevoirServiceTest {

    @Mock
    private DevoirRepository devoirRepository;

    @Mock
    private CourRepository courRepository;

    @InjectMocks
    private DevoirService devoirService;

    private DevoirDTO devoirDTO;
    private Cour cour;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialiser les objets de test
        devoirDTO = new DevoirDTO();
        devoirDTO.setTypedevoir("Examen");
        devoirDTO.setDescription("Description du devoir");
        devoirDTO.setPonderation(0.3f);
        devoirDTO.setBareme("text");
        devoirDTO.setDateLimite(new Date());
        devoirDTO.setStatut("Ouvert");

        // Créer un objet Cour
        cour = new Cour();
        cour.setIdCours(1);

        // Simuler la récupération du cours à partir de l'ID
        when(courRepository.findById(anyInt())).thenReturn(Optional.of(cour));
    }

   
}
	