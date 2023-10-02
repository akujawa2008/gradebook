package com.britenet.gradebook.grade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeRequestDto {

    private LocalDateTime createdAt;
    private BigDecimal value;
    private String description;
    private BigDecimal weight;

    @NotNull
    private Long studentId;
    @NotNull
    private Long subjectId;

}


