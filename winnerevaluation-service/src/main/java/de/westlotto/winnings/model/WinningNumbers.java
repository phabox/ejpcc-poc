package de.westlotto.winnings.model;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    private Draw draw;

    private boolean approved;

    @ElementCollection
    private List<WinningNumber> winningNumbers;

}