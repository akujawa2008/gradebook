package com.britenet.gradebook.classroom;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.UsersType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class ClassroomServiceTest {

    @InjectMocks
    private ClassroomService classroomService;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private ClassroomMapper classroomMapper;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addStudentToClassShouldAddStudentToClass() {
        Users student = Users.builder()
                .id(1L)
                .username("student")
                .password("pass123")
                .email("student@gmail.com")
                .role(UsersType.STUDENT)
                .build();

        Classroom classroom = Classroom.builder()
                .id(1L)
                .academicYear("2022/2023")
                .className("IVA")
                .build();

        AddStudentRequestDto request = new AddStudentRequestDto();
        request.setClassId(1L);
        request.setUsername("student");


        when(classroomRepository.findById(request.getClassId())).thenReturn(Optional.of(classroom));
        when(usersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(student));

        classroomService.addStudentToClass(request);

        assertEquals(student.getClassroom(), classroom);
        assertTrue(classroom.getStudents().contains(student));
    }

    @Test
    public void addClassShouldSaveClass() {
        ClassroomRequestDto dto = new ClassroomRequestDto();
        Classroom classroom = new Classroom();

        when(classroomRepository.findByClassName(dto.getClassName())).thenReturn(Optional.empty());
        when(classroomMapper.map(dto)).thenReturn(classroom);

        Classroom savedClassroom = new Classroom();
        when(classroomRepository.save(classroom)).thenReturn(savedClassroom);

        Classroom result = classroomService.addClass(dto);

        assertEquals(savedClassroom, result);
    }

    @Test
    public void removeClassShouldReturnSuccess() {
        when(classroomRepository.existsById(anyLong())).thenReturn(true);

        classroomService.removeClass(1L);

        verify(classroomRepository, times(1)).deleteById(1L);
    }

    @Test
    public void removeClassShouldReturnNotFound() {
        when(classroomRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> classroomService.removeClass(1L));
    }

}