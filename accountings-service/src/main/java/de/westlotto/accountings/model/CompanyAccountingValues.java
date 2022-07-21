package de.westlotto.accountings.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CompanyAccountingValues {

    @ManyToOne
    private Company company;

    private Long stake;

    private BigDecimal payout;
}
