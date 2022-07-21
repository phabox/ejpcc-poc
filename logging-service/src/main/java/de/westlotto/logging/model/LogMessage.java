package de.westlotto.logging.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class LogMessage {

    @Id
    @GeneratedValue
    private Long id;

    String serviceName;

    String time;

    String direction;

    String action;

    String eventName;

    @Column(length = 2048)
    String event;

    String type;

}
