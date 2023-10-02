package com.britenet.gradebook.classroom;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.users.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClassroomControllerTest {

    @InjectMocks
    private ClassroomController classroomController;

    @Mock
    private ClassroomService classroomService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addStudentToClassShouldReturnOk() {
        AddStudentRequestDto dto = new AddStudentRequestDto();

        ResponseEntity<Users> response = classroomController.addStudentToClass(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addStudentToClassShouldReturnBadRequest_WhenExceptionThrown() {
        AddStudentRequestDto dto = new AddStudentRequestDto();

        doThrow(new IllegalArgumentException("Error")).when(classroomService).addStudentToClass(dto);

        ResponseEntity<Users> response = classroomController.addStudentToClass(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addClassShouldReturnCreated() {
        ClassroomRequestDto dto = new ClassroomRequestDto();
        Classroom classroom = new Classroom();

        when(classroomService.addClass(dto)).thenReturn(classroom);

        ResponseEntity<Classroom> response = classroomController.addClass(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(classroom, response.getBody());
    }

    @Test
    public void removeClassShouldReturnSuccess() {
        ResponseEntity<Void> response = classroomController.removeClass(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(classroomService, times(1)).removeClass(1L);
    }

    @Test
    public void removeClassShouldReturnNotFound() {
        doThrow(new ResourceNotFoundException("Classroom not found")).when(classroomService).removeClass(1L);

        ResponseEntity<Void> response = classroomController.removeClass(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeClassShouldReturnBadRequest() {
        doThrow(new IllegalArgumentException()).when(classroomService).removeClass(1L);

        ResponseEntity<Void> response = classroomController.removeClass(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}