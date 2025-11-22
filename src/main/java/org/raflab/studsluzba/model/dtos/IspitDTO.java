package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitDTO {

    private Long id;
    private LocalDate datumOdrzavanja;
    private LocalTime vremePocetka;
    private boolean zakljucen;

    private Long predmetId;
    private String predmetNaziv;

    private Long nastavnikId;
    private String nastavnikImePrezime;

    private Long ispitniRokId;
    private String ispitniRokNaziv;
}
