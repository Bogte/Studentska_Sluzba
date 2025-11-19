package org.raflab.studsluzba.model.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IspitNepolozeniDTO {
    private String predmet;
    private LocalDate datum;
    private Integer ukupanBrojPoena;
}
