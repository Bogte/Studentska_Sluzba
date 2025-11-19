package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UplataDTO {

    private Long id;
    private LocalDate datumUplate;
    private double iznosRsd;
    private double srednjiKurs;
}
