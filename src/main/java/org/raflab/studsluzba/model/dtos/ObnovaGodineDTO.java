package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ObnovaGodineDTO {
    private Long id;
    private int godinaKojaSeObnavlja;
    private LocalDate datumObnove;
    private String napomena;
    private List<String> predmeti;
}
