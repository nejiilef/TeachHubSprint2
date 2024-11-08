package com.project.app.dto;

import lombok.Data;

@Data
public class DevoirRenduResponse {
	
	private Long idDevoirRendu;
	 private byte[] pdf;
	 private String email;
	 
}
