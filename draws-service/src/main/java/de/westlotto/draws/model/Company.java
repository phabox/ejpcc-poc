package de.westlotto.draws.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Company {
    @Id
    private Long id;
}
