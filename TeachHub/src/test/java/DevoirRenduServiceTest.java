
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.project.app.dto.DevoirRenduDTO;
import com.project.app.dto.DevoirRenduResponse;
import com.project.app.models.Devoir;
import com.project.app.models.DevoirRendu;
import com.project.app.models.Etudiant;
import com.project.app.repository.DevoirRepository;
import com.project.app.repository.DevoirRenduRepository;
import com.project.app.repository.EtudiantRepository;
import com.project.app.services.jwt.DevoirRenduService;

public class DevoirRenduServiceTest {

    @InjectMocks
    private DevoirRenduService devoirRenduService;

    @Mock
    private DevoirRepository devoirRepository;

    @Mock
    private DevoirRenduRepository devoirRenduRepository;

    @Mock
    private EtudiantRepository etudiantRepository;
    
    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);
    }
    @Test
    public void testMapToEntity() {
        DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
        devoirRenduDTO.setIdDevoirRendu(1L);

        DevoirRendu result = devoirRenduService.mapToEntity(devoirRenduDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdDevoirRendu());
    }
    
    @Test
    public void testAddDevoirRendu() {
        DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
        devoirRenduDTO.setIdDevoirRendu(1L);
        devoirRenduDTO.setPdf(new byte[]{1, 2, 3});

        Devoir devoir = new Devoir();
        devoir.setIdDevoir(1L);

        Etudiant etudiant = new Etudiant();
        etudiant.setEmail("etudiant@example.com");

        when(devoirRepository.findById(1L)).thenReturn(Optional.of(devoir));
        when(etudiantRepository.findByEmail("etudiant@example.com")).thenReturn(Optional.of(etudiant));
        when(devoirRenduRepository.save(any(DevoirRendu.class))).thenReturn(new DevoirRendu());

        DevoirRendu result = devoirRenduService.addDevoirRendu(devoirRenduDTO, 1L, "etudiant@example.com");

        assertNotNull(result);
        verify(devoirRenduRepository, times(1)).save(any(DevoirRendu.class));
    }

    @Test
    public void testCheckDevoirRendu() {
        Devoir devoir = new Devoir();
        devoir.setIdDevoir(1L);

        DevoirRendu devoirRendu = new DevoirRendu();
        devoirRendu.setDevoir(devoir);

        Etudiant etudiant = new Etudiant();
        etudiant.setEmail("etudiant@example.com");
        devoirRendu.setEtudiant(etudiant);

        when(devoirRenduRepository.findAll()).thenReturn(Arrays.asList(devoirRendu));

        boolean result = devoirRenduService.checkDevoirRendu(1L, "etudiant@example.com");

        assertTrue(result);
    }
    
    @Test
    public void testModifierDevoirRendu() {
        DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
        devoirRenduDTO.setPdf(new byte[]{1, 2, 3});

        Devoir devoir = new Devoir();
        devoir.setIdDevoir(1L);

        Etudiant etudiant = new Etudiant();
        etudiant.setEmail("etudiant@example.com");

        DevoirRendu devoirRendu = new DevoirRendu();
        devoirRendu.setDevoir(devoir);
        devoirRendu.setEtudiant(etudiant);

        when(devoirRenduRepository.findAll()).thenReturn(Arrays.asList(devoirRendu));
        when(devoirRenduRepository.save(any(DevoirRendu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DevoirRendu result = devoirRenduService.modifierDevoirRendu(1L, "etudiant@example.com", devoirRenduDTO);

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result.getPdf());
        verify(devoirRenduRepository, times(1)).save(any(DevoirRendu.class));
    }

    @Test
    public void testGetDevoirPDF() {
        Devoir devoir = new Devoir();
        devoir.setIdDevoir(1L);

        Etudiant etudiant = new Etudiant();
        etudiant.setEmail("etudiant@example.com");

        DevoirRendu devoirRendu = new DevoirRendu();
        devoirRendu.setDevoir(devoir);
        devoirRendu.setEtudiant(etudiant);
        devoirRendu.setPdf(new byte[]{1, 2, 3});

        when(devoirRenduRepository.findAll()).thenReturn(Arrays.asList(devoirRendu));

        byte[] result = devoirRenduService.getDevoirPDF(1L, "etudiant@example.com");

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }
    
    @Test
    public void testGetAllDevoirsRenduEnseignant() {
        Devoir devoir = new Devoir();
        devoir.setIdDevoir(1L);

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setEmail("etudiant1@example.com");

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setEmail("etudiant2@example.com");

        DevoirRendu devoirRendu1 = new DevoirRendu();
        devoirRendu1.setDevoir(devoir);
        devoirRendu1.setEtudiant(etudiant1);
        devoirRendu1.setPdf(new byte[]{1, 2, 3});
        devoirRendu1.setIdDevoirRendu(1L);

        DevoirRendu devoirRendu2 = new DevoirRendu();
        devoirRendu2.setDevoir(devoir);
        devoirRendu2.setEtudiant(etudiant2);
        devoirRendu2.setPdf(new byte[]{4, 5, 6});
        devoirRendu2.setIdDevoirRendu(2L);

        when(devoirRenduRepository.findAll()).thenReturn(Arrays.asList(devoirRendu1, devoirRendu2));

        List<DevoirRenduResponse> result = devoirRenduService.getAllDevoirsRenduEnseignant(1L);

        assertEquals(2, result.size());

        DevoirRenduResponse response1 = result.get(0);
        assertEquals(1L, response1.getIdDevoirRendu());
        assertEquals("etudiant1@example.com", response1.getEmail());
        assertArrayEquals(new byte[]{1, 2, 3}, response1.getPdf());

        DevoirRenduResponse response2 = result.get(1);
        assertEquals(2L, response2.getIdDevoirRendu());
        assertEquals("etudiant2@example.com", response2.getEmail());
        assertArrayEquals(new byte[]{4, 5, 6}, response2.getPdf());
    }
}