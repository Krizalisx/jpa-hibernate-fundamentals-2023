package com.tutorial.entities.keys;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ProductKey implements Serializable {

    private String code;

    private Long number;
}
