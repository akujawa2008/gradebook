package com.britenet.gradebook.classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    Optional<Classroom> findByClassName(String className);
}

