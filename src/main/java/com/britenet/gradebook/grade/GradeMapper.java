package com.britenet.gradebook.grade;

import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.subject.SubjectRepository;

import com.britenet.gradebook.users.UsersRepository;

import com.britenet.gradebook.users.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GradeMapper {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "student", source = "studentId", qualifiedByName = "idToStudent")
    @Mapping(target = "subject", source = "subjectId", qualifiedByName = "idToSubject")
    public abstract Grade map(GradeRequestDto gradeRequestDto);

    @Named("idToStudent")
    public Users idToStudent(Long studentId) {
        return studentId != null ? usersRepository.findById(studentId).orElseThrow() : null;
    }

    @Named("idToSubject")
    public Subject idToSubject(Long subjectId) {
        return subjectId != null ? subjectRepository.findById(subjectId).orElseThrow() : null;
    }

}
