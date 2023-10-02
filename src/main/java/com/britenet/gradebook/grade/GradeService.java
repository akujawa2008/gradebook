package com.britenet.gradebook.grade;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.subject.SubjectRepository;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRepository;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.UsersType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    private final GradeRepository gradeRepository;

    private final UsersRepository usersRepository;

    private final GradeMapper gradeMapper;

    private final SubjectRepository subjectRepository;

    public Grade addGrade(GradeRequestDto grade, String teacher) {

        if (!subjectRepository.existsById(grade.getSubjectId())) {
            throw new ResourceNotFoundException("Subject with ID " + grade.getSubjectId() + " not found.");
        }

        if (!usersRepository.existsById(grade.getStudentId())) {
            throw new UserNotFoundException("User with ID " + grade.getStudentId() + " not found.");
        }

        Users currentTeacher = usersRepository.findByUsername(teacher)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + teacher));
        isTeacherAssignedToSubject(currentTeacher, grade.getSubjectId());

        Grade gradeToSave = gradeMapper.map(grade);
        return gradeRepository.save(gradeToSave);
    }

    public List<Grade> findGradesByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        return gradeRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    public BigDecimal calculateFinalGradeForStudentInSubject(Long studentId, Long subjectId) {

        validateUserRoleIsStudent(studentId);
        BigDecimal sumOfWeightedGrades;
        BigDecimal sumOfWeights;

        List<Grade> grades = gradeRepository.findByStudentIdAndSubjectId(studentId, subjectId);
        sumOfWeightedGrades = BigDecimal.ZERO;
        sumOfWeights = BigDecimal.ZERO;

        for (Grade grade : grades) {
            sumOfWeightedGrades = sumOfWeightedGrades.add(grade.getValue().multiply(grade.getWeight()));
            sumOfWeights = sumOfWeights.add(grade.getWeight());
        }

        if (sumOfWeights.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Sum of weights cannot be zero");
        }

        return sumOfWeightedGrades.divide(sumOfWeights, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageGradeForClassAndSubject(Long classId, Long subjectId) {

        List<Grade> grades = gradeRepository.findByStudentClassroomIdAndSubjectId(classId, subjectId);

        BigDecimal totalWeightedGrade = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (Grade grade : grades) {
            totalWeightedGrade = totalWeightedGrade.add(grade.getValue().multiply(grade.getWeight()));
            totalWeight = totalWeight.add(grade.getWeight());
        }

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalWeightedGrade.divide(totalWeight, 2, RoundingMode.HALF_UP);
    }

    public Map<Long, BigDecimal> getAverageGradesForStudent(Long studentId) {

        validateUserRoleIsStudent(studentId);
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        Map<Long, BigDecimal> averageGrades = new HashMap<>();
        Map<Long, BigDecimal> totalWeights = new HashMap<>();

        for (Grade grade : grades) {
            Long subjectId = grade.getSubject().getId();
            BigDecimal previousTotal = averageGrades.getOrDefault(subjectId, BigDecimal.ZERO);
            BigDecimal previousWeight = totalWeights.getOrDefault(subjectId, BigDecimal.ZERO);

            averageGrades.put(subjectId, previousTotal.add(grade.getValue().multiply(grade.getWeight())));
            totalWeights.put(subjectId, previousWeight.add(grade.getWeight()));
        }

        for (Long subjectId : averageGrades.keySet()) {
            BigDecimal total = averageGrades.get(subjectId);
            BigDecimal totalWeight = totalWeights.get(subjectId);

            if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
                averageGrades.put(subjectId, BigDecimal.ZERO);
            } else {
                averageGrades.put(subjectId, total.divide(totalWeight, 2, RoundingMode.HALF_UP));
            }
        }

        return averageGrades;
    }

    public BigDecimal getOverallAverageForStudent(Long studentId) {
        Map<Long, BigDecimal> averageGrades = getAverageGradesForStudent(studentId);
        BigDecimal totalAverage = BigDecimal.ZERO;
        for (BigDecimal average : averageGrades.values()) {
            totalAverage = totalAverage.add(average);
        }
        if (averageGrades.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return totalAverage.divide(new BigDecimal(averageGrades.size()), 2, RoundingMode.HALF_UP);
    }

    public void validateUserRoleIsStudent(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Student with id " + id + " not found"));
        if (!UsersType.STUDENT.equals(user.getRole())){
            throw new UnauthorizedException("Checking grades is only possible for the user type 'STUDENT'");
        }
    }

    public Boolean isTeacherAssignedToSubject(Users user, Long subjectId) {
        boolean isAssigned = teacherAssignmentRepository.findByTeacher(user)
                .stream()
                .filter(teacherAssignment -> teacherAssignment.getSubject() != null)
                .anyMatch(teacherAssignment -> subjectId.equals(teacherAssignment.getSubject().getId()));
        if (!isAssigned) {
            throw new UnauthorizedException("Teacher is not assigned to provided subject");
        } else {
            return true;
        }
    }
}