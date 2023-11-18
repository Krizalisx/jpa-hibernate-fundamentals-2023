package com.tutorial.entities;

import com.tutorial.entities.keys.ProductKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Setter
@Table(name = "product")
@IdClass(ProductKey.class)
public class Product {

    @Id
    private String code;

    @Id
    private Long number;

    private String color;
}
