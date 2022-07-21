package de.westlotto.winningnumbers.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Draw {
    @Id
    private Long id;

    private DrawState drawState;

}
