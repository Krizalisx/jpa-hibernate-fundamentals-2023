package com.tutorial.entities.keys;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class StudentKey implements Serializable {

    private Long number;
    private String code;
}
