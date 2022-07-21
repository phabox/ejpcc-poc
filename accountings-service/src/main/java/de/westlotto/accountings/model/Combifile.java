package de.westlotto.accountings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Combifile {
    private Draw draw;

    private Long companyId;

    private Long amount;
}
