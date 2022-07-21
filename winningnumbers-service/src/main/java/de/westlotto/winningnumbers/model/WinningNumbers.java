package de.westlotto.winningnumbers.model;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WinningNumbers {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    private Draw draw;

    private boolean approved;

    @ElementCollection
    private List<WinningNumber> winningNumbers;

    public void approve(Draw draw, boolean approved) {
        if (draw.getDrawState() != DrawState.WINNING_NUMBERS_RECEIVED) {
            throw new RuntimeException(String.format("Drawstate has to be %s to approve winning numbers, but it is %s",
                    DrawState.WINNING_NUMBERS_RECEIVED.name(), draw.getDrawState().name()));
        }
        this.approved = approved;
    }
}