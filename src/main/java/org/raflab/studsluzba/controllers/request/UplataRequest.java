package org.raflab.studsluzba.controllers.request;

import lombok.Data;

@Data
public class UplataRequest {

    private int brojIndeksa;
    private int godinaUpisa;

    private double iznosRsd;
}
