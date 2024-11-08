package com.project.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.dto.SousGroupeDTO;
import com.project.app.models.Cour;
import com.project.app.models.Etudiant;
import com.project.app.models.SousGroupe;
import com.project.app.repository.CourRepository;
import com.project.app.repository.EtudiantRepository;
import com.project.app.services.jwt.SousGroupeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SousGroupeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SousGroupeService sousGroupeService;

    @MockBean
    private EtudiantRepository etudiantRepository;

    @MockBean
    private CourRepository courRepository; 
    @InjectMocks
    private SousGroupeController sousGroupeController;

    private SousGroupeDTO sousGroupeDTO;

    @BeforeEach
    void setUp() {
        sousGroupeDTO = new SousGroupeDTO();
        sousGroupeDTO.setIdSousGroupe(1);
        sousGroupeDTO.setNom("Group A");
        sousGroupeDTO.setEtudiants(Arrays.asList("email1@test.com", "email2@test.com"));
        
        Cour c=new Cour();
        c.setIdCours(1);
        
        when(courRepository.save(c)).thenReturn(c);
        // Ensure the students exist in the database
        Etudiant student1 = new Etudiant();
        student1.setEmail("email1@test.com");
        student1.setNom("Student1");
        student1.setPrenom("Test");
        when(etudiantRepository.save(student1)).thenReturn(student1);

        Etudiant student2 = new Etudiant();
        student2.setEmail("email2@test.com");
        student2.setNom("Student2");
        student2.setPrenom("Test");
        when(etudiantRepository.save(student2)).thenReturn(student2);
    }
    
    @Test
    void testAddSousGroupe() throws Exception {
    	
    	
        // Prepare mock behavior
        SousGroupe mockSousGroupe = new SousGroupe();
        mockSousGroupe.setNom(sousGroupeDTO.getNom());
        mockSousGroupe.setIdSousGroupe(1); // Ensure this is set

        when(sousGroupeService.addSousGroupe(sousGroupeDTO, 1)).thenReturn(mockSousGroupe);

        // Perform the test
        ResultActions result = mockMvc.perform(post("/addsousgroupe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sousGroupeDTO)));

        // Log the response for debugging
        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        System.out.println("JSON Response: " + jsonResponse);

        // Assertions
        result.andExpect(status().isOk());
    }

    @Test
    void testModifierSousGroupe() throws Exception {
        // Prepare mock behavior
        SousGroupe mockSousGroupe = new SousGroupe();
        mockSousGroupe.setNom("Updated Group");
        mockSousGroupe.setIdSousGroupe(1);  // Ensure this is set

        when(sousGroupeService.modifierSousGroupe(1, sousGroupeDTO)).thenReturn(mockSousGroupe);

        // Perform the test
        ResultActions result = mockMvc.perform(put("/updatesousgroupe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sousGroupeDTO)));

        // Log the response for debugging
        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        System.out.println("JSON Response: " + jsonResponse);

        result.andExpect(status().isOk());
    }



    @Test
    void testGetAllSousGroupes() throws Exception {
        // Prepare mock behavior
        SousGroupe mockSousGroupe = new SousGroupe();
        mockSousGroupe.setNom("Group A");
        mockSousGroupe.setIdSousGroupe(1);

        when(sousGroupeService.getAllSousGroupes(1)).thenReturn(List.of(mockSousGroupe));

        // Perform the test
        ResultActions result = mockMvc.perform(get("/sousgroupe/1"));

        result.andExpect(status().isOk())
              .andExpect(jsonPath("$[0].idSousGroupe").value(1))
              .andExpect(jsonPath("$[0].nom").value("Group A"));
    }

    @Test
    void testAddEtudiantSousGroupe() throws Exception {
        // Prepare mock behavior
        SousGroupe mockSousGroupe = new SousGroupe();
        mockSousGroupe.setNom("Group A");
        mockSousGroupe.setIdSousGroupe(1);

        when(sousGroupeService.addEtudiantSousGroupe(1, "email1@test.com")).thenReturn(mockSousGroupe);

        // Perform the test
        ResultActions result = mockMvc.perform(post("/addEtudiantSousgroupe/1/email1@test.com"));

        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.idSousGroupe").value(1))
              .andExpect(jsonPath("$.nom").value("Group A"));
    }

    @Test
    void testDeleteSousGroupe() throws Exception {
        // Prepare mock behavior
        doNothing().when(sousGroupeService).deleteSousGroupe(1);

        // Perform the test
        ResultActions result = mockMvc.perform(delete("/deletesousgroupe/1"));

        result.andExpect(status().isOk())
              .andExpect(content().string("sous groupe deleted successfully"));
    }

   
}
