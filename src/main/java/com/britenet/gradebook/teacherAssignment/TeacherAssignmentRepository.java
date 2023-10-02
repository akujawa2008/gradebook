package com.britenet.gradebook.teacherAssignment;

import com.britenet.gradebook.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {

    List<TeacherAssignment> findByTeacher(Users teacher);
}