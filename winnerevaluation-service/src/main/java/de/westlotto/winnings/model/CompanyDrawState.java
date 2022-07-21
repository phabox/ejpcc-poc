package de.westlotto.winnings.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class CompanyDrawState {
    @ManyToOne
    private Company company;
    private CompanyState companyState;
}
