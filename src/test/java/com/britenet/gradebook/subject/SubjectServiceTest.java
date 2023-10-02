package com.britenet.gradebook.subject;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class SubjectServiceTest {

    @InjectMocks
    private SubjectService subjectService;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addSubjectShouldSaveSubject() {
        SubjectRequestDto dto = new SubjectRequestDto();
        dto.setSubjectName("Math");

        Subject subject = new Subject();
        when(subjectMapper.map(dto)).thenReturn(subject);
        when(subjectRepository.findBySubjectName(dto.getSubjectName())).thenReturn(Optional.empty());

        Subject savedSubject = new Subject();
        when(subjectRepository.save(subject)).thenReturn(savedSubject);

        Subject result = subjectService.addSubject(dto);

        assertEquals(savedSubject, result);
    }

    @Test
    public void addSubjectShouldThrowExceptionWhenSubjectNameExists() {
        SubjectRequestDto dto = new SubjectRequestDto();
        dto.setSubjectName("Math");

        when(subjectRepository.findBySubjectName(dto.getSubjectName())).thenReturn(Optional.of(new Subject()));

        assertThrows(IllegalArgumentException.class, () -> subjectService.addSubject(dto));
    }

    @Test
    public void deleteSubjectSuccess() {
        when(subjectRepository.existsById(anyLong())).thenReturn(true);

        subjectService.deleteSubject(1L);

        verify(subjectRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteSubjectNotFound() {
        when(subjectRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> subjectService.deleteSubject(1L));
    }

}