package com.project.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.dto.DevoirRenduDTO;
import com.project.app.models.DevoirRendu;
import com.project.app.services.jwt.DevoirRenduService;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
public class DevoirRenduControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevoirRenduService devoirRenduService;

    @InjectMocks
    private DevoirRenduController devoirRenduController;

    private DevoirRenduDTO devoirRenduDTO;

    @BeforeEach
    void setUp() {
        devoirRenduDTO = new DevoirRenduDTO();
        devoirRenduDTO.setPdf(new byte[]{1, 2, 3}); // Example PDF content
    }

    @Test
    void testAddDevoirRendu() throws Exception {
        // Prepare mock behavior
        DevoirRendu mockDevoirRendu = new DevoirRendu();
        mockDevoirRendu.setIdDevoirRendu(1L);;

        when(devoirRenduService.addDevoirRendu(devoirRenduDTO, 1L, "email@test.com")).thenReturn(mockDevoirRendu);

        // Perform the test
        ResultActions result = mockMvc.perform(post("/addDevoirRendu/1/email@test.com")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(new ObjectMapper().writeValueAsString(devoirRenduDTO)));

        // Log the response for debugging
        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        System.out.println("JSON Response: " + jsonResponse);

        result.andExpect(status().isOk());
    }

    @Test
    void testGetAllDevoirsRendu() throws Exception {
        // Prepare mock behavior
        DevoirRendu mockDevoirRendu = new DevoirRendu();
        mockDevoirRendu.setIdDevoirRendu(1L);;

        when(devoirRenduService.getAllDevoirsRendu(1L)).thenReturn(List.of(mockDevoirRendu));

        // Perform the test
        ResultActions result = mockMvc.perform(get("/DevoirRendu/1"));

        result.andExpect(status().isOk());
    }

    @Test
    void testDownloadDevoirPDF() throws Exception {
        // Prepare mock behavior
        byte[] pdfData = new byte[]{1, 2, 3};

        when(devoirRenduService.getDevoirPDF(1L, "email@test.com")).thenReturn(pdfData);

        // Perform the test
        ResultActions result = mockMvc.perform(get("/devoirRendu/download/1/email@test.com"));

        result.andExpect(status().isOk())
              .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devoirRendu_1.pdf"))
              .andExpect(content().contentType(MediaType.APPLICATION_PDF))
              .andExpect(content().bytes(pdfData));
    }

    @Test
    void testModifierDevoirRendu() throws Exception {
        // Prepare mock behavior
        DevoirRendu mockDevoirRendu = new DevoirRendu();
        mockDevoirRendu.setIdDevoirRendu(1L);;

        when(devoirRenduService.modifierDevoirRendu(1L, "email@test.com", devoirRenduDTO)).thenReturn(mockDevoirRendu);

        // Perform the test
        ResultActions result = mockMvc.perform(put("/updateDevoirRendu/1/email@test.com")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(new ObjectMapper().writeValueAsString(devoirRenduDTO)));

        result.andExpect(status().isOk());
    }

    @Test
    void testDeleteDevoirRendu() throws Exception {
        // Prepare mock behavior
        doNothing().when(devoirRenduService).deleteDevoirRendu(1L, "email@test.com");

        // Perform the test
        ResultActions result = mockMvc.perform(delete("/deleteDevoirRendu/1/email@test.com"));

        result.andExpect(status().isOk())
              .andExpect(content().string("Devoir rendu deleted successfully"));
    }

    @Test
    void testCheckDevoirRendu() throws Exception {
        // Prepare mock behavior
        when(devoirRenduService.checkDevoirRendu(1L, "email@test.com")).thenReturn(true);

        // Perform the test
        ResultActions result = mockMvc.perform(get("/CheckDevoirRendu/1/email@test.com"));

        result.andExpect(status().isOk())
              .andExpect(content().string("devoir deja rendu"));
    }
}
