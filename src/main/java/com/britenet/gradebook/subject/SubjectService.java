package com.britenet.gradebook.subject;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper;

    public Subject addSubject(final SubjectRequestDto subjectDto) {
        if (subjectRepository.findBySubjectName(subjectDto.getSubjectName()).isPresent()) {
            throw new IllegalArgumentException("Subject with the same name already exists!");
        }

        Subject subject = subjectMapper.map(subjectDto);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(final Long subjectId) {
        if(!subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException("Subject not found");
        }
        subjectRepository.deleteById(subjectId);
    }

}


