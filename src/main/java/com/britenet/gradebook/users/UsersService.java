package com.britenet.gradebook.users;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.subject.SubjectRepository;
import com.britenet.gradebook.teacherAssignment.TeacherAssignment;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRepository;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.britenet.gradebook.users.UsersType.STUDENT;
import static com.britenet.gradebook.users.UsersType.TEACHER;


@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    private final SubjectRepository subjectRepository;

    private final UsersMapper usersMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TeacherAssignment assignTeacherToSubject(TeacherAssignmentRequest request) {

        Users teacher = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + request.getUsername()));

        if (TEACHER.equals(teacher.getRole())) {
            Subject subject = subjectRepository.findBySubjectName(request.getSubjectName())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Subject not found for teacher: %s", teacher.getUsername())));

            TeacherAssignment assignment = TeacherAssignment.builder()
                    .teacher(teacher)
                    .subject(subject)
                    .build();

            return teacherAssignmentRepository.save(assignment);
        } else {
            throw new UnauthorizedException("Only 'TEACHER' role can be assigned as TeacherAssignment");
        }
    }

    @Transactional
    public Users addStudent(UsersRequestDto student) {
        if (!STUDENT.name().equals(student.getRole())) {
            throw new IllegalArgumentException("Role must be STUDENT for this operation");
        }
        return saveUser(student);
    }

    private Users saveUser(final UsersRequestDto student) {
        if (usersRepository.findByUsernameOrEmail(student.getUsername(), student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exist!");
        }

        Users user = usersMapper.map(student);
        user.setPassword(passwordEncoder.encode(student.getPassword()));

        return usersRepository.save(user);
    }

    @Transactional
    public Users addTeacher(UsersRequestDto teacher) {
        if (!TEACHER.name().equals(teacher.getRole())) {
            throw new IllegalArgumentException("Role must be TEACHER for this operation");
        }
        return saveUser(teacher);
    }

    public void removeUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
        usersRepository.deleteById(id);
    }

    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(" User with username %s not found", username)));
    }
}