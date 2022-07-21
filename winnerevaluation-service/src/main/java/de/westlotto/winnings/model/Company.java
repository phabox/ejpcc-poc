package de.westlotto.winnings.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Company {
    @Id
    private Long id;
}
