    package org.raflab.studsluzba.model;

    import lombok.Data;

    import javax.persistence.*;
    import java.time.LocalDate;
    import java.util.List;

    @Entity
    @Data
    public class UpisGodine {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private int godinaKojaSeUpisuje;
        private LocalDate datumUpisa;
        private String napomena;

        @ManyToOne
        private StudentIndeks studentIndeks;

        @ManyToMany
        private List<Predmet> prenetiPredmeti;
    }
