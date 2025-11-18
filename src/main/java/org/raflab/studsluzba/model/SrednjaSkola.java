package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class SrednjaSkola {

    @Id
    @GeneratedValue
    private Long id;

    private String naziv;
    private String mesto;
    private String vrsta; // gimnazija ili strucna
}
