package org.raflab.studsluzba.model;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class IspitPrijava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datumPrijave;
    private Integer predispitniPoeni;
    private Integer poeniSaIspita;
    private Integer ukupnoPoena;
    private Integer ocena;
    private boolean polozen;
    private boolean ponistio;
    private boolean priznat;
    private String napomena;

    @ManyToOne
    private Ispit ispit;

    @ManyToOne
    private StudentIndeks studentIndeks;

}
