package de.westlotto.accountings.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinnerEvaluationResult {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Draw draw;

    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = true)
    private Company company;

    private Long totalWinners;

    boolean approved;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<WinnerCount> winnerCounts;

}
