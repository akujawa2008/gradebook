package com.britenet.gradebook.teacherAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAssignmentRequest {

    private String username;
    private String subjectName;

}
