package com.tutorial.entities;

import com.tutorial.entities.keys.StudentKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student")
public class Student {

    @EmbeddedId
    private StudentKey id;

    private String name;
}
