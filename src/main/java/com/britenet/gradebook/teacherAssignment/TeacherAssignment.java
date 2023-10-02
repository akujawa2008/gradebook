package com.britenet.gradebook.teacherAssignment;

import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Users teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

}