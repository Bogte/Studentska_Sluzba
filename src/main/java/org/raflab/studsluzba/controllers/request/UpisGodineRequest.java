package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class UpisGodineRequest {

    private int brojIndeksa;
    private int godinaUpisa;

    private int godinaKojaSeUpisuje;
    private String napomena;
}
