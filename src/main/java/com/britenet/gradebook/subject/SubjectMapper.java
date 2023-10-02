package com.britenet.gradebook.subject;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubjectMapper {

    Subject map(SubjectRequestDto subjectRequestDto);
}