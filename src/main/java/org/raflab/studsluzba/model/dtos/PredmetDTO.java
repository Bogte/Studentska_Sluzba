package org.raflab.studsluzba.model.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PredmetDTO {
    @NotNull
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private boolean obavezan;
    private String studProgramNaziv;
}
