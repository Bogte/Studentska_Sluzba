package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PredispitniPoeniDTO {
    private String predmet;
    private int predispitniPoeni;
    private Long indeksId;
    private int brojIndeksa;
    private int godinaUpisa;
    private String studijskiProgram;
    private LocalDate datum;
}
