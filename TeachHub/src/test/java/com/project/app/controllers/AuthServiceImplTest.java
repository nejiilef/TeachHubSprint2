package com.project.app.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.app.dto.SignUpRequest;
import com.project.app.models.Etudiant;
import com.project.app.repository.EtudiantRepository;
import com.project.app.services.jwt.AuthServiceImpl;

public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateEtudiant_EmailDoesNotExist() {
    	// Préparation des données de test
        SignUpRequest signUpRequest = new SignUpRequest("Doe", "John", "test@example.com", "password", "ROLE_ETUDIANT");
        
        // Mocker le comportement du repository pour simuler un email non utilisé
        when(etudiantRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        
        // Mocker le comportement du password encoder
        when(passwordEncoder.encode(signUpRequest.getMotDePasse())).thenReturn("hashedPassword");
        
        // Simuler le comportement du repository pour retourner un étudiant lors de l'appel de save
        Etudiant mockEtudiant = new Etudiant();
        mockEtudiant.setId(1L); // Simuler un ID après la sauvegarde
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(mockEtudiant);

        // Appel de la méthode à tester
        Etudiant createdEtudiant = authService.createEtudiant(signUpRequest);
        
        // Vérifications
        assertNotNull(createdEtudiant); // L'étudiant doit être créé
        assertEquals(1L, createdEtudiant.getId()); // Vérifier l'ID
        verify(etudiantRepository).save(any(Etudiant.class));
    }

    
    @Test
    public void testCreateEtudiant_EmailAlreadyExists() {
    	
        // Préparation des données de test
        SignUpRequest signUpRequest = new SignUpRequest("", "John", "", "password", "ROLE_ETUDIANT");
        
        // Mocker le comportement du repository pour simuler un email déjà utilisé
        when(etudiantRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);
        
        // Appel de la méthode à tester
        Etudiant createdEtudiant = authService.createEtudiant(signUpRequest);
        
        // Vérifications
        assertNull(createdEtudiant);
        
        // Vérifiez que le repository n'a pas été appelé pour sauvegarder l'étudiant
        verify(etudiantRepository, never()).save(any(Etudiant.class));
    }
}
