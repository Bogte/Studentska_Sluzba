package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class IspitIzlazakRequest {

    private Integer predispitniPoeni;
    private Integer poeniSaIspita;
    private String napomena;
}
