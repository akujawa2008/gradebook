package com.britenet.gradebook.classroom;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassroomMapper {

    Classroom map(ClassroomRequestDto classroomRequestDto);
}