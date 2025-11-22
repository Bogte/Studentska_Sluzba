package org.raflab.studsluzba.model.dtos;

import lombok.Data;

@Data
public class PrijavljeniStudentDTO {

    private Long prijavaId;

    private Long studentId;
    private String ime;
    private String prezime;

    private Long indeksId;
    private int broj;
    private int godinaUpisa;
    private String studProgramOznaka;

    private int osvojeniPoeni;
    private Integer ocena;
}
