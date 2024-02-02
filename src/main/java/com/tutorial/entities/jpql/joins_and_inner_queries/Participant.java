package com.tutorial.entities.jpql.joins_and_inner_queries;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table(name = "participant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NamedQuery(name = "getAll", query = "select p from Participant p")
@NamedQuery(name = "getByName", query = "select p from Participant p where p.name = :name")
@NamedEntityGraph(
    name = "Participant.enrollments",
    attributeNodes = {
        @NamedAttributeNode("enrollments")
    })
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "participant")
    @Exclude
    private List<Enrollment> enrollments;
}
