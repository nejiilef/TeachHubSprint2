package com.project.app.services.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import com.project.app.dto.DevoirRenduDTO;
import com.project.app.dto.DevoirRenduResponse;
import com.project.app.models.Devoir;
import com.project.app.models.DevoirRendu;
import com.project.app.models.Etudiant;
import com.project.app.repository.DevoirRenduRepository;
import com.project.app.repository.DevoirRepository;
import com.project.app.repository.EtudiantRepository;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.transaction.Transactional;

@Service
public class DevoirRenduService implements IDevoirRenduService{

	@Autowired
    private DevoirRepository devoirRepository;
	@Autowired
    private DevoirRenduRepository devoirRenduRepository;
	@Autowired
    private EtudiantRepository etudiantRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	
	
	public DevoirRendu mapToEntity(DevoirRenduDTO DevoirDTO) {
		DevoirRendu dev=new DevoirRendu();
		dev.setIdDevoirRendu(DevoirDTO.getIdDevoirRendu());
		return dev;
	}
	
	@Transactional
	@Override
	public DevoirRendu addDevoirRendu(DevoirRenduDTO DevoirRenduDTO, Long idDevoir, String emailEtudiant) {
		DevoirRendu devR = this.mapToEntity(DevoirRenduDTO); // Convert DTO to Entity

		Devoir dev=this.devoirRepository.findById(idDevoir).orElseThrow();
		Etudiant et=this.etudiantRepository.findByEmail(emailEtudiant).orElseThrow();
		devR.setIdDevoirRendu(idDevoir);
		devR.setEtudiant(et);
		devR.setDevoir(dev);
		 if (DevoirRenduDTO.getPdf() != null && DevoirRenduDTO.getPdf().length != 0) {
		        devR.setPdf(DevoirRenduDTO.getPdf());  // Setting the PDF in the entity 'dev'
		    }

		
		
		return this.devoirRenduRepository.save(devR);
	}

	public boolean checkDevoirRendu(Long idDevoir, String email) {
	    return this.getAllDevoirsRendu(idDevoir).stream()
	               .anyMatch(devoirR -> devoirR.getEtudiant().getEmail().equals(email));
	}

	@Override
	public List<DevoirRendu> getAllDevoirsRendu(Long idDevoir) {
		// TODO Auto-generated method stub
		List<DevoirRendu> l=new ArrayList<>();
		 this.devoirRenduRepository.findAll().stream()
		        .collect(Collectors.toList()).forEach(d->{
		        if(d.getDevoir().getIdDevoir()==idDevoir) {
		        	l.add(d);
		        }
		        });
		 return l;
	}
	public List<DevoirRenduResponse> getAllDevoirsRenduEnseignant(Long idDevoir) {
		// TODO Auto-generated method stub
		List<DevoirRenduResponse> l=new ArrayList<>();
		 this.devoirRenduRepository.findAll().stream()
		        .collect(Collectors.toList()).forEach(d->{
		        if(d.getDevoir().getIdDevoir()==idDevoir) {
		        	DevoirRenduResponse res=new DevoirRenduResponse();
		        	res.setIdDevoirRendu(d.getIdDevoirRendu());
		        	res.setEmail(d.getEtudiant().getEmail());
		        	res.setPdf(d.getPdf());
		        	l.add(res);
		        }
		        });
		 return l;
	}

	@Transactional
	@Override
	public DevoirRendu modifierDevoirRendu(Long idDevoir, String email, DevoirRenduDTO devoirRenduDTO) {
	    DevoirRendu dev = new DevoirRendu();
	    AtomicBoolean found = new AtomicBoolean(false); // Use AtomicBoolean to allow modification

	    // Iterate through the list of submitted assignments
	    this.getAllDevoirsRendu(idDevoir).forEach(d -> {
	        if (d.getEtudiant().getEmail().equals(email)) {
	            // Found a matching assignment
	            found.set(true); // Set the boolean to true
	            dev.setDevoir(d.getDevoir());
	            dev.setEtudiant(d.getEtudiant());
	            dev.setIdDevoirRendu(d.getIdDevoirRendu());
	            if (devoirRenduDTO.getPdf() != null && devoirRenduDTO.getPdf().length != 0) {
	                dev.setPdf(devoirRenduDTO.getPdf()); // Setting the PDF in the entity 'dev'
	            }
	        }
	    });

	    if (!found.get()) { // Check the value of found
	        // Handle the case where the assignment was not found
	        throw new IllegalArgumentException("Devoir not found for the given email");
	    }

	    return this.devoirRenduRepository.save(dev); // Save the updated assignment
	}


	@Transactional
	public byte[] getDevoirPDF(Long idDevoir, String email) {
	    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
	    AtomicReference<byte[]> pdfContent = new AtomicReference<>(null);

	    try {
	        for (DevoirRendu d : this.getAllDevoirsRendu(idDevoir)) {
	            if (d.getEtudiant().getEmail().equals(email)) {
	                if (d != null && d.getPdf() != null) {
	                    pdfContent.set(d.getPdf()); // set the PDF content
	                    break; // exit the loop once the PDF is found
	                }
	            }
	        }
	    } finally {
	        transactionManager.commit(status);
	    }

	    return pdfContent.get();
	}

	@Override
	public void deleteDevoirRendu(Long idDevoir,String email) {
		 this.getAllDevoirsRendu(idDevoir).forEach(d -> {
		        if (d.getEtudiant().getEmail().equals(email)) {
		this.devoirRenduRepository.delete(d);
		        }});
		
	}

}
