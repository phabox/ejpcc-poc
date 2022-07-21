package de.westlotto.accountings.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountingValues {

    @Transient
    private static final Map<Integer, Integer> SHARE_DISTRIBUTION = Map.of(1, 45,
            2, 30,
            3, 15,
            4, 7,
            5, 2,
            6, 1);

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Draw draw;

    private Long totalStake;

    private Long totalWinners;

    private BigDecimal leftoverStake;

    boolean approved;

    @ElementCollection
    List<CompanyAccountingValues> companyAccountingValues;

    public void approve() {
        this.approved = true;
    }

    public void calculateAccountings(List<WinnerEvaluationResult> winnerEvaluationResults,
            List<CompanyStake> companyStakes) {

        WinnerEvaluationResult aggregateWinnerEvaluation = winnerEvaluationResults.stream()
                .filter(e -> e.getCompany() == null).findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("No aggregation winner result found for draw id %d",
                                this.draw.getId())));

        Map<Integer, Long> totalWinnersPerClass = aggregateWinnerEvaluation.getWinnerCounts().stream()
                .collect(Collectors.toMap(WinnerCount::getWinningClass, WinnerCount::getCount));

        this.totalStake = companyStakes.stream().mapToLong(CompanyStake::getStake).sum();
        this.totalWinners = totalWinnersPerClass.entrySet().stream().mapToLong(e -> e.getValue()).sum();

        Map<Long, Long> stakePerCompany = companyStakes.stream()
                .collect(Collectors.toMap(e -> e.getCompany().getId(), e -> e.getStake()));

        Map<Integer, BigDecimal> payoutPerClass = IntStream.range(1, 7).boxed()
                .collect(Collectors.toMap(Function.identity(),
                        winningClass -> new BigDecimal(totalStake)
                                .multiply(new BigDecimal(SHARE_DISTRIBUTION
                                        .get(winningClass)).divide(
                                                new BigDecimal(100)))));

        this.companyAccountingValues = winnerEvaluationResults.stream().filter(e -> e.getCompany() != null)
                .map(e -> {
                    Long companyStake = stakePerCompany.get(e.getCompany().getId());

                    BigDecimal totalCompanyPayout = new BigDecimal(0);
                    for (AtomicInteger winningClass = new AtomicInteger(1); winningClass.intValue() <= 6; winningClass
                            .getAndIncrement()) {

                        BigDecimal totalClassPayout = payoutPerClass
                                .get(winningClass.intValue());
                        Long totalClassWinners = totalWinnersPerClass
                                .get(winningClass.intValue());
                        Long companyClassWinners = e.getWinnerCounts().stream()
                                .filter(wc -> wc.getWinningClass()
                                        .equals(winningClass.intValue()))
                                .map(c -> c.getCount()).findFirst().orElse(0l);

                        if (0l != totalClassWinners) {
                            totalCompanyPayout = totalCompanyPayout.add(totalClassPayout
                                    .multiply(new BigDecimal(companyClassWinners)
                                            .divide(new BigDecimal(totalClassWinners), 2, RoundingMode.HALF_EVEN)));
                        }
                    }

                    return new CompanyAccountingValues(e.getCompany(), companyStake,
                            totalCompanyPayout);
                }).collect(Collectors.toList());

        this.leftoverStake = new BigDecimal(totalStake);
        this.companyAccountingValues
                .forEach((e -> this.leftoverStake = this.leftoverStake.subtract(e.getPayout())));
    }

}
