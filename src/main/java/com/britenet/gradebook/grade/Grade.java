package com.britenet.gradebook.grade;

import com.britenet.gradebook.subject.Subject;
import com.britenet.gradebook.users.Users;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @Column
    @Size(max = 6, min = 1)
    private BigDecimal value;

    @Column
    private String description;

    @Column
    @Size(max = 6, min = 1)
    private BigDecimal weight;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users student;

}