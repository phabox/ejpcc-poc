package de.westlotto.draws.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class CompanyDrawState {

    @ManyToOne(cascade = CascadeType.DETACH)
    private Company company;
    private CompanyState companyState;

}
