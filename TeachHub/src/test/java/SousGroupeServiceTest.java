import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.app.dto.SousGroupeDTO;
import com.project.app.models.Cour;
import com.project.app.models.Etudiant;
import com.project.app.models.SousGroupe;
import com.project.app.repository.CourRepository;
import com.project.app.repository.EtudiantRepository;
import com.project.app.repository.SousGroupeRepository;
import com.project.app.services.jwt.SousGroupeService;

public class SousGroupeServiceTest {

    @InjectMocks
    private SousGroupeService sousGroupeService;

    @Mock
    private SousGroupeRepository sousGroupeRepository;

    @Mock
    private CourRepository courRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSousGroupe() {
        SousGroupeDTO sousGroupeDTO = new SousGroupeDTO();
        sousGroupeDTO.setIdSousGroupe(1);
        sousGroupeDTO.setNom("Groupe 1");
        sousGroupeDTO.setEtudiants(Arrays.asList("etudiant1@example.com", "etudiant2@example.com"));

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setEmail("etudiant1@example.com");
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setEmail("etudiant2@example.com");

        Cour cour = new Cour();
        cour.setIdCours(1);

        when(etudiantRepository.findByEmail("etudiant1@example.com")).thenReturn(Optional.of(etudiant1));
        when(etudiantRepository.findByEmail("etudiant2@example.com")).thenReturn(Optional.of(etudiant2));
        when(courRepository.findById(1)).thenReturn(Optional.of(cour));
        when(sousGroupeRepository.save(any(SousGroupe.class))).thenReturn(new SousGroupe());

        SousGroupe result = sousGroupeService.addSousGroupe(sousGroupeDTO, 1);

        assertNotNull(result);
        verify(sousGroupeRepository, times(1)).save(any(SousGroupe.class));
    }

    @Test
    public void testGetAllSousGroupes() {
        Cour cour = new Cour();
        cour.setIdCours(1);

        SousGroupe sousGroupe1 = new SousGroupe();
        sousGroupe1.setCour(cour);
        SousGroupe sousGroupe2 = new SousGroupe();
        sousGroupe2.setCour(cour);

        when(sousGroupeRepository.findAll()).thenReturn(Arrays.asList(sousGroupe1, sousGroupe2));

        List<SousGroupe> result = sousGroupeService.getAllSousGroupes(1);

        assertEquals(2, result.size());
    }
    
    @Test
    public void testAddEtudiantSousGroupe() {
        SousGroupe sousGroupe = new SousGroupe();
        sousGroupe.setIdSousGroupe(1);
        Cour cour = new Cour();
        cour.setIdCours(1);
        sousGroupe.setCour(cour);

        Etudiant etudiant = new Etudiant();
        etudiant.setEmail("etudiant@example.com");

        // Ajoutez l'étudiant au cours
        cour.setStudents(Set.of(etudiant));

        when(sousGroupeRepository.findById(1)).thenReturn(Optional.of(sousGroupe));
        when(courRepository.findById(1)).thenReturn(Optional.of(cour));
        when(etudiantRepository.findByEmail("etudiant@example.com")).thenReturn(Optional.of(etudiant));
        when(sousGroupeRepository.save(any(SousGroupe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ajoutez des assertions pour vérifier l'état avant l'appel de la méthode
        assertTrue(cour.getStudents().contains(etudiant), "The student should be in the course");

        SousGroupe result = sousGroupeService.addEtudiantSousGroupe(1, "etudiant@example.com");

        assertNotNull(result, "The result should not be null");
        assertTrue(result.getEtudiants().contains(etudiant), "The student should be added to the sous-groupe");
        verify(sousGroupeRepository, times(1)).save(sousGroupe);
    }

    @Test
    public void testModifierSousGroupe() {
        SousGroupeDTO sousGroupeDTO = new SousGroupeDTO();
        sousGroupeDTO.setNom("Nouveau Nom");
        sousGroupeDTO.setEtudiants(Arrays.asList("etudiant1@example.com", "etudiant2@example.com"));

        SousGroupe sousGroupe = new SousGroupe();
        sousGroupe.setIdSousGroupe(1);
        sousGroupe.setNom("Ancien Nom");

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setEmail("etudiant1@example.com");
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setEmail("etudiant2@example.com");

        when(sousGroupeRepository.findById(1)).thenReturn(Optional.of(sousGroupe));
        when(etudiantRepository.findByEmail("etudiant1@example.com")).thenReturn(Optional.of(etudiant1));
        when(etudiantRepository.findByEmail("etudiant2@example.com")).thenReturn(Optional.of(etudiant2));

        SousGroupe result = sousGroupeService.modifierSousGroupe(1, sousGroupeDTO);

        assertNotNull(result);
        assertEquals("Nouveau Nom", result.getNom());
        assertEquals(2, result.getEtudiants().size());
    }
    @Test
    public void testMapToEntity() {
        SousGroupeDTO sousGroupeDTO = new SousGroupeDTO();
        sousGroupeDTO.setIdSousGroupe(1);
        sousGroupeDTO.setNom("Groupe 1");
        sousGroupeDTO.setEtudiants(Arrays.asList("etudiant1@example.com", "etudiant2@example.com"));

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setEmail("etudiant1@example.com");
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setEmail("etudiant2@example.com");

        when(etudiantRepository.findByEmail("etudiant1@example.com")).thenReturn(Optional.of(etudiant1));
        when(etudiantRepository.findByEmail("etudiant2@example.com")).thenReturn(Optional.of(etudiant2));

        SousGroupe result = sousGroupeService.mapToEntity(sousGroupeDTO);

        assertNotNull(result);
        assertEquals(1, result.getIdSousGroupe());
        assertEquals("Groupe 1", result.getNom());
        assertEquals(2, result.getEtudiants().size());
        assertTrue(result.getEtudiants().contains(etudiant1));
        assertTrue(result.getEtudiants().contains(etudiant2));
    }
}
