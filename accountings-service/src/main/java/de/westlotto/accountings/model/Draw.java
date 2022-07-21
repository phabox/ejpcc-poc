package de.westlotto.accountings.model;

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

    private DrawState drawState;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<CompanyDrawState> companyDrawStates;
}
