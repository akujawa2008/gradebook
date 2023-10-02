package com.britenet.gradebook.classroom;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.UsersType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassroomService {


    private final ClassroomRepository classroomRepository;

    private final ClassroomMapper classroomMapper;

    private final UsersRepository usersRepository;

    @Transactional
    public void addStudentToClass(final AddStudentRequestDto request) {
        Classroom classroom = classroomRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found!"));

        Users student = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Student with username " + request.getUsername() + " not found"));

        if (!UsersType.STUDENT.equals(student.getRole())) {
            throw new UnauthorizedException("Only user with role 'STUDENT' can be added to classroom");
        }
        if (classroom.getStudents().contains(student)) {
            throw new IllegalStateException("Student with username " + request.getUsername() + " is already added to the classroom");
        }

        student.setClassroom(classroom);
        classroom.getStudents().add(student);
    }

    public Classroom addClass(final ClassroomRequestDto classroomRequestDto) {

        if (classroomRepository.findByClassName(classroomRequestDto.getClassName()).isPresent()) {
            throw new IllegalArgumentException("Class with the same name already exists!");
        }
        Classroom classroom = classroomMapper.map(classroomRequestDto);

        return classroomRepository.save(classroom);
    }

    public void removeClass(final Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Class with ID " + id + " not found.");
        }
        classroomRepository.deleteById(id);
    }

}
