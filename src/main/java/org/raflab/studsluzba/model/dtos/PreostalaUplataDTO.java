package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreostalaUplataDTO {

    private double ukupnoUplacenoEur;
    private double preostaloEur;
    private double preostaloRsd;
    private double trenutniKurs;
}
