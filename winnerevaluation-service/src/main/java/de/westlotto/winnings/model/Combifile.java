package de.westlotto.winnings.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Combifile {

    @Id
    private Long id;

    @ManyToOne
    private Draw draw;

    private Long companyId;

    private Long amount;

    private boolean confirmedByCompany;

    private boolean approvedByCC;

    @Lob
    private byte[] data;
}
