package de.westlotto.combifiles.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Draw draw;

    private Long companyId;

    private String filename;

    private String type;

    private Long amount;

    private String checksum;

    private boolean confirmedByCompany;

    private boolean approvedByCC;

    @Lob
    private byte[] data;
}
