package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IspitPolozenDTO {

    private String predmet;
    private int ocena;
    private LocalDate datum;
}
