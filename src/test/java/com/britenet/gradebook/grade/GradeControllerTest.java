package com.britenet.gradebook.grade;

import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GradeControllerTest {

    @InjectMocks
    private GradeController gradeController;

    @Mock
    private GradeService gradeService;

    @Mock
    private UsersService usersService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAverageGradeForClassAndSubject() {
        when(gradeService.getAverageGradeForClassAndSubject(1L, 1L)).thenReturn(BigDecimal.TEN);

        ResponseEntity<BigDecimal> response = gradeController.getAverageGradeForClassAndSubject(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, response.getBody());
    }

    @Test
    public void testGetAverageGradesForStudent() {
        Map<Long, BigDecimal> expectedGrades = new HashMap<>();
        expectedGrades.put(1L, BigDecimal.TEN);
        when(gradeService.getAverageGradesForStudent(1L)).thenReturn(expectedGrades);

        ResponseEntity<Map<Long, BigDecimal>> response = gradeController.getAverageGradesForStudent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGrades, response.getBody());
    }

    @Test
    public void testGetFinalGradeForStudentInSubject() {
        when(gradeService.calculateFinalGradeForStudentInSubject(1L, 1L)).thenReturn(BigDecimal.TEN);

        ResponseEntity<BigDecimal> response = gradeController.getFinalGradeForStudentInSubject(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, response.getBody());
    }

    @Test
    public void testGetOverallAverageForStudent() {
        when(gradeService.getOverallAverageForStudent(1L)).thenReturn(BigDecimal.TEN);

        ResponseEntity<BigDecimal> response = gradeController.getOverallAverageForStudent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, response.getBody());
    }

    @Test
    public void testAddGrade() {
        GradeRequestDto requestDto = new GradeRequestDto(); // assume you have default values set
        Grade grade = new Grade();
        when(authentication.getName()).thenReturn("testTeacher");
        when(gradeService.addGrade(requestDto, "testTeacher")).thenReturn(grade);

        ResponseEntity<Grade> response = gradeController.addGrade(requestDto, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grade, response.getBody());
    }

    @Test
    public void testAddGradeUnauthorized() {
        GradeRequestDto requestDto = new GradeRequestDto();
        when(authentication.getName()).thenReturn("testTeacher");
        when(gradeService.addGrade(requestDto, "testTeacher")).thenThrow(UnauthorizedException.class);

        ResponseEntity<Grade> response = gradeController.addGrade(requestDto, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testGetGradesForStudentBySubject() {
        when(authentication.getName()).thenReturn("testTeacher");
        when(gradeService.isTeacherAssignedToSubject(any(), anyLong())).thenReturn(true);
        List<Grade> grades = Collections.singletonList(new Grade());
        when(gradeService.findGradesByStudentIdAndSubjectId(1L, 1L)).thenReturn(grades);

        ResponseEntity<List<Grade>> response = gradeController.getGradesForStudentBySubject(1L, 1L, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
    }

    @Test
    public void testGetGradesForLoggedInStudentAndSubject() {
        when(authentication.getName()).thenReturn("testStudent");
        Users user = new Users();
        user.setId(1L);
        when(usersService.findByUsername("testStudent")).thenReturn(user);
        List<Grade> grades = Collections.singletonList(new Grade());
        when(gradeService.findGradesByStudentIdAndSubjectId(1L, 1L)).thenReturn(grades);

        ResponseEntity<List<Grade>> response = gradeController.getGradesForLoggedInStudentAndSubject(authentication, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
    }
}