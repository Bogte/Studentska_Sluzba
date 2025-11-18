package org.raflab.studsluzba.model.dtos;


import lombok.Data;

/**
 * 
 * Entitet koji se koristi za prenos osnovnih podataka o studentu, 
 * mogu da budu samo licni podaci, bez indeksa, u slučaju
 * da je student unet u sistem ali mu još nije dodeljen indeks
 * ili podaci sa aktivnim indeksom. 
 * 
 * Koristi se kao rezultat pretrage studenata
 * 
 * @author bojanads
 *
 */


@Data
public class StudentDTO {

	private Long id; //Studnet podaci

	private String ime;
	private String prezime;
	private String srednjeIme;
	private String jmbg;
	private String email;

	// Indeks podaci
	private Long idIndeks;
	private int godinaUpisa;
	private String studProgramOznaka;
	private int broj;
	private boolean aktivanIndeks;

	private Long studijskiProgramId;

}
