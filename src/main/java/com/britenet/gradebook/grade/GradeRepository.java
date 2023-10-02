package com.britenet.gradebook.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudentClassroomIdAndSubjectId(Long classroomId, Long subjectId);

    List<Grade> findByStudentIdAndSubjectId(Long userId, Long subjectId);


}
