package de.westlotto.accountings.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyStake {
    @GeneratedValue
    @Id
    private Long id;

    private Long stake;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Draw draw;
}
