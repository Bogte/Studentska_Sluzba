package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class IspitniRok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    @ManyToOne
    private SkolskaGodina skolskaGodina;

    @OneToMany(mappedBy = "ispitniRok", cascade = CascadeType.ALL)
    private List<Ispit> ispiti;
}
