package org.raflab.studsluzba.model;

import jdk.jfr.Enabled;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class SkolskaGodina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oznaka; // "2017/2018"
    private boolean aktivna;
}
