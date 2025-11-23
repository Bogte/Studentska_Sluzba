package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class RezultatIspitaDTO {

    private String ime;
    private String prezime;

    private String studProgramOznaka;
    private int godinaUpisa;
    private int broj;

    private int predispitniPoeni;
    private int poeniSaIspita;
    private int ukupnoPoena;

    private boolean polozen;
}
