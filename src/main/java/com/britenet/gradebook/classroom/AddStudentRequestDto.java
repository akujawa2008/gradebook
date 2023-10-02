package com.britenet.gradebook.classroom;

import lombok.Data;

@Data
public class AddStudentRequestDto {
    private Long classId;
    private String username;
}
