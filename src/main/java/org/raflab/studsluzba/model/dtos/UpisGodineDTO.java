package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpisGodineDTO {

    private Long id;
    private int godinaKojaSeUpisuje;
    private LocalDate datumUpisa;
    private String napomena;
    private List<String> prenetiPredmeti;
}
