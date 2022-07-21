package de.westlotto.companies.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Company {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String abbreviation;
    private String email;
    private String phone;
    private String fax;
}
