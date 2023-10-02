package com.britenet.gradebook.subject;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/admin/addSubject")
    @Operation(summary = "Add subject", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add subject succeeded"),
            @ApiResponse(responseCode = "400", description = "Add subject failed")})
    public ResponseEntity<Subject> addSubject(@RequestBody SubjectRequestDto subject) {
        if(log.isInfoEnabled()) {
            log.info("Request to add subject: " + subject);
        }
        try {
            Subject savedSubject = subjectService.addSubject(subject);
            return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/deleteSubject/{subjectId}")
    @Operation(summary = "Delete subject", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "400", description = "Delete subject failed")
    })
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        if(log.isInfoEnabled()) {
            log.info("Request to delete subject with ID: " + subjectId);
        }
        try {
            subjectService.deleteSubject(subjectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
