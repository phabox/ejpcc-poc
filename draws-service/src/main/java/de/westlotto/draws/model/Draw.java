package de.westlotto.draws.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.Data;

@Data
@Entity
public class Draw {
    @GeneratedValue
    @Id
    private Long id;
    private LocalDate drawDate;
    private DrawState drawState;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<CompanyDrawState> companyDrawStates;
}
