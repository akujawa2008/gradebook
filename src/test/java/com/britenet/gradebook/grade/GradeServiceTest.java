package com.britenet.gradebook.grade;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.subject.SubjectRepository;
import com.britenet.gradebook.teacherAssignment.TeacherAssignment;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRepository;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.UsersType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @InjectMocks
    private GradeService gradeService;

    @Mock
    private TeacherAssignmentRepository teacherAssignmentRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GradeMapper gradeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGrade() {
        Users teacher = Users.builder()
                .username("teacher")
                .password("pass123")
                .email("teacher@test.pl")
                .role(UsersType.TEACHER)
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .subjectName("Math")
                .build();

        TeacherAssignment teacherAssignment = TeacherAssignment.builder()
                .teacher(teacher)
                .subject(subject)
                .build();

        GradeRequestDto requestDto = GradeRequestDto.builder()
                .createdAt(LocalDateTime.now())
                .description("test")
                .weight(new BigDecimal("5"))
                .studentId(1L)
                .subjectId(1L)
                .value(new BigDecimal("1"))
                .build();

        Grade grade = new Grade();

        when(usersRepository.findByUsername(eq("teacher"))).thenReturn(Optional.of(teacher));
        when(teacherAssignmentRepository.findByTeacher(eq(teacher))).thenReturn(List.of(teacherAssignment));
        when(subjectRepository.existsById(any())).thenReturn(true);
        when(usersRepository.existsById(any())).thenReturn(true);
        when(gradeMapper.map(eq(requestDto))).thenReturn(grade);
        when(gradeRepository.save(eq(grade))).thenReturn(grade);

        gradeService.addGrade(requestDto, "teacher");

        verify(gradeRepository, times(1)).save(eq(grade));
    }

    @Test
    void findGradesByStudentIdAndSubjectId() {
        Grade grade = new Grade();
        when(gradeRepository.findByStudentIdAndSubjectId(1L, 1L)).thenReturn(Collections.singletonList(grade));
        gradeService.findGradesByStudentIdAndSubjectId(1L, 1L);
        verify(gradeRepository).findByStudentIdAndSubjectId(1L, 1L);
    }

    @Test
    void calculateFinalGradeForStudentInSubject() {
        Users student = new Users();
        student.setRole(UsersType.STUDENT);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(student));
        Grade grade = new Grade();
        grade.setValue(new BigDecimal("5"));
        grade.setWeight(new BigDecimal("1"));
        when(gradeRepository.findByStudentIdAndSubjectId(1L, 1L)).thenReturn(Collections.singletonList(grade));

        gradeService.calculateFinalGradeForStudentInSubject(1L, 1L);
    }

    @Test
    void validateUserRoleIsStudentValidRole() {
        Users student = new Users();
        student.setRole(UsersType.STUDENT);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(student));
        gradeService.validateUserRoleIsStudent(1L);
    }

    @Test
    void validateUserRoleIsStudentInvalidRole() {
        Users user = new Users();
        user.setRole(UsersType.TEACHER);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        try {
            gradeService.validateUserRoleIsStudent(1L);
        } catch (UnauthorizedException e) {
            // Expected
        }
    }

    @Test
    void isTeacherAssignedToSubjectNotAssigned() {
        Users teacher = new Users();
        when(teacherAssignmentRepository.findByTeacher(teacher)).thenReturn(Collections.emptyList());

        try {
            gradeService.isTeacherAssignedToSubject(teacher, 1L);
        } catch (UnauthorizedException e) {
            // Expected
        }
    }

    @Test
    void addGradeTeacherNotAssignedToSubject() {
        Users teacher = new Users();

        GradeRequestDto requestDto = GradeRequestDto.builder()
                .createdAt(LocalDateTime.now())
                .description("test")
                .weight(new BigDecimal("5"))
                .studentId(1L)
                .subjectId(1L)
                .value(new BigDecimal("1"))
                .build();

        when(subjectRepository.existsById(any())).thenReturn(true);
        when(usersRepository.existsById(any())).thenReturn(true);
        when(usersRepository.findByUsername(eq("testTeacher"))).thenReturn(Optional.of(teacher));
        when(teacherAssignmentRepository.findByTeacher(eq(teacher))).thenReturn(Collections.emptyList());

        assertThrows(UnauthorizedException.class, () -> gradeService.addGrade(requestDto, "testTeacher"));
    }

    @Test
    void addGradeStudentNotFound() {
        GradeRequestDto requestDto = GradeRequestDto.builder()
                .createdAt(LocalDateTime.now())
                .description("test")
                .weight(new BigDecimal("5"))
                .studentId(1L)
                .subjectId(1L)
                .value(new BigDecimal("1"))
                .build();

        when(subjectRepository.existsById(any())).thenReturn(true);
        when(usersRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> gradeService.addGrade(requestDto, "testTeacher"));
    }

}