package com.britenet.gradebook.users;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.teacherAssignment.TeacherAssignment;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UsersControllerTest {

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void addStudentIfValidRequestShouldReturnCreated() throws Exception {
        UsersRequestDto studentRequest = new UsersRequestDto();
        studentRequest.setUsername("student1");
        studentRequest.setPassword("password123");
        studentRequest.setEmail("student1@example.com");
        studentRequest.setRole(UsersType.STUDENT.toString());

        Users savedStudent = new Users();
        savedStudent.setUsername("student1");
        savedStudent.setPassword("password123");
        savedStudent.setEmail("student1@example.com");
        savedStudent.setRole(UsersType.STUDENT);

        when(usersService.addStudent(any(UsersRequestDto.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/api/admin/addStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addTeacherIfValidRequestShouldReturnCreated() throws Exception {
        UsersRequestDto teacherRequest = new UsersRequestDto();
        teacherRequest.setUsername("teacher1");
        teacherRequest.setPassword("password123");
        teacherRequest.setEmail("teacher1@example.com");
        teacherRequest.setRole(UsersType.TEACHER.toString());

        Users savedTeacher = new Users();
        savedTeacher.setUsername("teacher1");
        savedTeacher.setPassword("password123");
        savedTeacher.setEmail("teacher1@example.com");
        savedTeacher.setRole(UsersType.TEACHER);

        when(usersService.addTeacher(any(UsersRequestDto.class))).thenReturn(savedTeacher);

        mockMvc.perform(post("/api/admin/addTeacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void assignTeacherToSubjectIfValidRequestShouldReturnCreated() throws Exception {

        TeacherAssignmentRequest request = TeacherAssignmentRequest.builder()
                .username("teacher1")
                .subjectName("Math")
                .build();

        Users user = Users.builder()
                .username("teacher1")
                .password("pass123")
                .email("teacher1@gmail.com")
                .role(UsersType.TEACHER)
                .build();

        Subject subject = Subject.builder()
                .subjectName("Math")
                .build();

        TeacherAssignment assignment = new TeacherAssignment();
        assignment.setTeacher(user);
        assignment.setSubject(subject);

        when(usersService.assignTeacherToSubject(any(TeacherAssignmentRequest.class))).thenReturn(assignment);

        mockMvc.perform(post("/api/admin/assignTeacherToSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void removeUser_Success() {
        ResponseEntity<Void> response = usersController.removeUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usersService, times(1)).removeUser(1L);
    }

    @Test
    public void removeUser_NotFound() {
        doThrow(new ResourceNotFoundException("User not found")).when(usersService).removeUser(1L);

        ResponseEntity<Void> response = usersController.removeUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeUser_BadRequest() {
        doThrow(new IllegalArgumentException()).when(usersService).removeUser(1L);

        ResponseEntity<Void> response = usersController.removeUser(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}