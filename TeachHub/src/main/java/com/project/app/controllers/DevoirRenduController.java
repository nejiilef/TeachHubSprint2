package com.project.app.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.project.app.dto.DevoirRenduDTO;

import com.project.app.models.DevoirRendu;
import com.project.app.services.jwt.DevoirRenduService;


import jakarta.transaction.Transactional;

@RestController
public class DevoirRenduController {

	@Autowired
    private DevoirRenduService devoirRenduService;
	
	
	@Transactional
	 @PostMapping(value = "/addDevoirRendu/{idDevoir}/{email}")
	    public ResponseEntity<DevoirRendu> addDevoirRendu(
	            @RequestParam(value = "pdf", required = false) MultipartFile pdf,
	            @PathVariable Long idDevoir,
	            @PathVariable String email) {

	        DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
	      
	        // Gérer le fichier PDF
	        if (pdf != null && !pdf.isEmpty()) {
	            try {
	                devoirRenduDTO.setPdf(pdf.getBytes());
	            } catch (IOException e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	            }
	        }

	        DevoirRendu savedDevoir = devoirRenduService.addDevoirRendu(devoirRenduDTO, idDevoir,email);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedDevoir);
	    }
	
	@GetMapping(value = "/DevoirRendu/{idDevoir}")
    public List<DevoirRendu> getAllDevoirsRendu(@PathVariable Long idDevoir) {
        return devoirRenduService.getAllDevoirsRendu(idDevoir); 
    }
	
	
	
	@GetMapping("/devoirRendu/download/{idDevoir}/{email}")
    public ResponseEntity<ByteArrayResource> downloadDevoirPDF(@PathVariable Long idDevoir,@PathVariable String email) {
        byte[] pdfData = devoirRenduService.getDevoirPDF(idDevoir,email);
        if (pdfData != null) {
            ByteArrayResource resource = new ByteArrayResource(pdfData);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devoirRendu_" + idDevoir + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
        
       
   }

	 @PutMapping("/updateDevoirRendu/{idDevoirRendu}/{email}")
	    public ResponseEntity<DevoirRendu> modifierDevoirRendu(@PathVariable Long idDevoirRendu,@PathVariable String email,@RequestParam(value = "pdf", required = false) MultipartFile pdf) {
		 DevoirRenduDTO devoirRenduDTO = new DevoirRenduDTO();
	      
	        // Gérer le fichier PDF
	        if (pdf != null && !pdf.isEmpty()) {
	            try {
	                devoirRenduDTO.setPdf(pdf.getBytes());
	            } catch (IOException e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	            }
	        }

		 DevoirRendu updatedDevoirRendu = devoirRenduService.modifierDevoirRendu(idDevoirRendu,email, devoirRenduDTO);
	        if (updatedDevoirRendu != null) {
	            return new ResponseEntity<>(updatedDevoirRendu, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	        }
	    }  
 
	 @DeleteMapping("/deleteDevoirRendu/{id}/{email}")
	 public ResponseEntity<String> deleteDevoir(@PathVariable(value="id") Long id,@PathVariable String email){
		 this.devoirRenduService.deleteDevoirRendu(id,email);
		 return ResponseEntity.status(HttpStatus.OK).body("Devoir rendu deleted successfully");
	 }
	 @GetMapping(value = "/CheckDevoirRendu/{idDevoir}/{email}")
	    public ResponseEntity<String> checkDevoirsRendu(@PathVariable Long idDevoir,@PathVariable String email) {
		 if(this.devoirRenduService.checkDevoirRendu(idDevoir, email)) {
	        return ResponseEntity.status(HttpStatus.OK).body("devoir deja rendu");
	    }else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("devoir n'est pas rendu");
	    }}
	 
}