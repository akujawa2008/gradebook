package com.britenet.gradebook.grade;

import com.britenet.gradebook.exception.UnauthorizedException;
import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    private final UsersService userService;

    @GetMapping("/teacher/average/class/{classId}/subject/{subjectId}")
    @Operation(summary = "Get average grade for class from particular subject", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get average grade for class succeeded"),
            @ApiResponse(responseCode = "404", description = "Average grade for class not found")})
    public ResponseEntity<BigDecimal> getAverageGradeForClassAndSubject(@PathVariable Long classId, @PathVariable Long subjectId) {
        if (log.isInfoEnabled()) {
            log.info("Request to get average grade for class: " + classId + " and subject: " + subjectId);
        }
        BigDecimal average = gradeService.getAverageGradeForClassAndSubject(classId, subjectId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/teacher/average/student/{studentId}")
    @Operation(summary = "Get average grade for student from each subject", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get average grade for student succeeded"),
            @ApiResponse(responseCode = "404", description = "Average grade for student not found")})
    public ResponseEntity<Map<Long, BigDecimal>> getAverageGradesForStudent(@PathVariable Long studentId) { // zwrotka do zmapowania na obiekty
        if (log.isInfoEnabled()) {
            log.info("Request to get average grades from each subject for user: " + studentId);
        }
        Map<Long, BigDecimal> averageGrades = gradeService.getAverageGradesForStudent(studentId);
        return ResponseEntity.ok(averageGrades);
    }

    @GetMapping("/teacher/finalGrade/{studentId}/{subjectId}")
    @Operation(summary = "Get final grade for student from particular subject", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get final grade succeeded"),
            @ApiResponse(responseCode = "404", description = "Final grade not found")})
    public ResponseEntity<BigDecimal> getFinalGradeForStudentInSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        if (log.isInfoEnabled()) {
            log.info("Request to get final grade for user: " + studentId + " from subject: " + subjectId);
        }
        BigDecimal finalGrade = gradeService.calculateFinalGradeForStudentInSubject(studentId, subjectId);
        return ResponseEntity.ok(finalGrade);
    }

    @GetMapping("/teacher/average/student/{studentId}/overall")
    @Operation(summary = "Get overall average grade for student", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get overall average grade for student succeed"),
            @ApiResponse(responseCode = "404", description = "Grades not found")})
    public ResponseEntity<BigDecimal> getOverallAverageForStudent(@PathVariable Long studentId) {
        if (log.isInfoEnabled()) {
            log.info("Request to get overall average for student: " + studentId);
        }
        BigDecimal overallAverage = gradeService.getOverallAverageForStudent(studentId);
        return ResponseEntity.ok(overallAverage);
    }

    @PostMapping("/teacher/addGrade")
    @Operation(summary = "Add grade", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add grade succeeded"),
            @ApiResponse(responseCode = "400", description = "Add grade failed")})
    public ResponseEntity<Grade> addGrade(@RequestBody @Valid GradeRequestDto requestDto, Authentication authentication) {
        if (log.isInfoEnabled()) {
            log.info("Request by user: " + authentication.getName() + " to add grade: " + requestDto);
        }

        String loggedTeacherUsername = authentication.getName();

        try {
            Grade addedGrade = gradeService.addGrade(requestDto, loggedTeacherUsername);
            return ResponseEntity.ok(addedGrade);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/teacher/{studentId}/subjects/{subjectId}")
    @Operation(summary = "Get student grade by teacher assigned to particular subject", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get student grade from particular subject succeeded"),
            @ApiResponse(responseCode = "404", description = "Student's grade not found")})
    public ResponseEntity<List<Grade>> getGradesForStudentBySubject(@PathVariable Long studentId, @PathVariable Long subjectId, Authentication authentication) {
        if (log.isInfoEnabled()) {
            log.info("Request by user: " + authentication.getName() + " to get grades for student: " + studentId + " from subject: " + subjectId);
        }

        Users user = userService.findByUsername(authentication.getName()); // TODO move logic to GradeService
        Boolean teacherAssignedToSubject = gradeService.isTeacherAssignedToSubject(user, subjectId);
        if (teacherAssignedToSubject) {
            return ResponseEntity.ok(gradeService.findGradesByStudentIdAndSubjectId(studentId, subjectId));
        } else {
            throw new UnauthorizedException("Teacher is not assigned to subject");
        }
    }

    @GetMapping("/student/subject/{subjectId}")
    @Operation(summary = "Get logged student grade from particular subject", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get grades succeed"),
            @ApiResponse(responseCode = "404", description = "Grades not found")})
    public ResponseEntity<List<Grade>> getGradesForLoggedInStudentAndSubject(Authentication authentication, @PathVariable Long subjectId) {
        if (log.isInfoEnabled()) {
            log.info("Request by logged in user: " + authentication.getName() + " to get grades from subject: " + subjectId);
        }
        String loggedInUsername = authentication.getName();
        Users user = userService.findByUsername(loggedInUsername);
        gradeService.validateUserRoleIsStudent(user.getId());
        return ResponseEntity.ok(gradeService.findGradesByStudentIdAndSubjectId(user.getId(), subjectId));
    }
}
