package com.britenet.gradebook.users;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;


    @Test
    public void addStudentWithInvalidRoleShouldThrowIllegalArgumentException() {
        UsersRequestDto student = new UsersRequestDto();
        student.setRole("ADMIN");

        assertThrows(IllegalArgumentException.class, () -> usersService.addStudent(student));
    }

    @Test
    public void addTeacherWithInvalidRoleShouldThrowIllegalArgumentException() {
        UsersRequestDto teacher = new UsersRequestDto();
        teacher.setRole("ADMIN");

        assertThrows(IllegalArgumentException.class, () -> usersService.addTeacher(teacher));
    }

    @Test
    public void assignTeacherToSubjectWithNonExistingUserShouldThrowUserNotFoundException() {
        TeacherAssignmentRequest request = TeacherAssignmentRequest.builder()
                .subjectName("Math")
                .username("ghost")
                .build();

        when(usersRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.assignTeacherToSubject(request));
    }

    @Test
    public void assignTeacherToSubjectWithNonTeacherRoleShouldThrowUnauthorizedException() {
        TeacherAssignmentRequest request = TeacherAssignmentRequest.builder()
                .subjectName("Math")
                .username("teacher1")
                .build();
        Users user = Users.builder()
                .username("teacher1")
                .password("123")
                .email("teacher11@gmail.com")
                .role(UsersType.STUDENT)
                .build();

        when(usersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> usersService.assignTeacherToSubject(request));
    }

    @Test
    public void removeUserSuccess() {
        when(usersRepository.existsById(1L)).thenReturn(true);

        usersService.removeUser(1L);

        verify(usersRepository, times(1)).deleteById(1L);
    }

    @Test
    public void removeUserNotFound() {
        when(usersRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.removeUser(1L);
        });
    }

}