package com.britenet.gradebook.subject;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

public class SubjectControllerTest {

    @InjectMocks
    private SubjectController subjectController;

    @Mock
    private SubjectService subjectService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addSubjectShouldReturnCreated() {
        SubjectRequestDto dto = SubjectRequestDto.builder()
                .subjectName("Math")
                .build();

        Subject savedSubject = Subject.builder()
                .subjectName("Math")
                .build();

        when(subjectService.addSubject(dto)).thenReturn(savedSubject);

        ResponseEntity<Subject> response = subjectController.addSubject(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedSubject, response.getBody());
    }

    @Test
    public void testDeleteSubjectSuccess() {
        doNothing().when(subjectService).deleteSubject(anyLong());

        ResponseEntity<Void> response = subjectController.deleteSubject(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteSubjectNotFound() {
        doThrow(new ResourceNotFoundException("Subject not found")).when(subjectService).deleteSubject(anyLong());

        ResponseEntity<Void> response = subjectController.deleteSubject(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}