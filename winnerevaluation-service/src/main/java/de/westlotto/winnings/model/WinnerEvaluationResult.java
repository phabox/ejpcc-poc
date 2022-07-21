package de.westlotto.winnings.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.CollectionUtils;

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
    @GeneratedValue
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

    public void evaluateWinners(Combifile combifile, WinningNumbers wnr) {
        if (combifile == null || wnr == null || CollectionUtils.isEmpty(wnr.getWinningNumbers())) {
            throw new RuntimeException("Combifile and winning numbers must be set");
        }
        String combinations = new String(combifile.getData());
        Set<Integer> winningNumbers = wnr.getWinningNumbers().stream().map(e -> e.getNumber())
                .collect(Collectors.toSet());

        this.winnerCounts = IntStream.range(1, winningNumbers.size() + 1).mapToObj(i -> WinnerCount.builder()
                .winningClass(i)
                .count(countNumberOfMatchingTips(winningNumbers, winningNumbers.size() - (i - 1), combinations))
                .build()).collect(Collectors.toList());

        this.totalWinners = winnerCounts.stream().mapToLong(e -> e.getCount()).sum();
    }

    private long countNumberOfMatchingTips(Set<Integer> winningNumbers, int requiredMatches, String combinations) {
        AtomicLong result = new AtomicLong(0);

        combinations.lines().forEach(line -> {
            String[] splitted = line.split("=");

            long numberOfMatches = Stream.of(splitted[0].split(" ")).map(Integer::parseInt)
                    .filter(winningNumbers::contains)
                    .count();
            if (numberOfMatches == requiredMatches) {
                result.addAndGet(Long.parseLong(splitted[1]));
            }
        });

        return result.longValue();
    }

    public void aggregateWinners(List<WinnerEvaluationResult> results) {
        this.winnerCounts = results.stream().flatMap(e -> e.getWinnerCounts().stream()).collect(
                Collectors.groupingBy(WinnerCount::getWinningClass, Collectors.summingLong(WinnerCount::getCount)))
                .entrySet().stream().map(e -> new WinnerCount(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        this.totalWinners = winnerCounts.stream().mapToLong(e -> e.getCount()).sum();
    }

    public void approve() {
        this.approved = true;
    }
}
