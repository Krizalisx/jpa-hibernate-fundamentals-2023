package com.tutorial.entities.jpql.joins_and_inner_queries.dto;

import com.tutorial.entities.jpql.joins_and_inner_queries.Enrollment;
import com.tutorial.entities.jpql.joins_and_inner_queries.Participant;

public record EnrolledStudent(

    Participant participant,
    Enrollment enrollment

) {}
