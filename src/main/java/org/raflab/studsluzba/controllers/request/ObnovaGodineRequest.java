package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import java.util.List;

@Data
public class ObnovaGodineRequest {

    private int brojIndeksa;
    private int godinaUpisa;

    private int godinaKojaSeObnavlja;

    private List<Long> idPredmetaIzNaredneGodine;
    private String napomena;
}
