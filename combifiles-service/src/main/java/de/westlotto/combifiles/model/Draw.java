package de.westlotto.combifiles.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Draw {
    @Id
    private Long id;

    /*
     * @Column(unique = true)
     * private Long drawId;
     */

    private DrawState drawState;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<CompanyDrawState> companyDrawStates;
}
