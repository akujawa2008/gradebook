package com.britenet.gradebook.classroom;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.users.Users;
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
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("/admin/addStudentToClass")
    @Operation(summary = "Add student to class", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add student to class succeeded"),
            @ApiResponse(responseCode = "400", description = "Add student to class failed")})
    public ResponseEntity<Users> addStudentToClass(@RequestBody AddStudentRequestDto addStudentRequestDto) {
        if(log.isInfoEnabled()) {
            log.info("Request to add student to class: " + addStudentRequestDto);
        }
        try {
            classroomService.addStudentToClass(addStudentRequestDto);
            return ResponseEntity
                    .ok()
                    .build();
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/addClass")
    @Operation(summary = "Add class", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add class succeeded"),
            @ApiResponse(responseCode = "400", description = "Add class failed")})
    public ResponseEntity<Classroom> addClass(@RequestBody ClassroomRequestDto classroom) {
        if(log.isInfoEnabled()) {
            log.info("Request to add class: " + classroom);
        }
        try {
            Classroom savedClassroom = classroomService.addClass(classroom);
            return new ResponseEntity<>(savedClassroom, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/admin/removeClass/{id}")
    @Operation(summary = "Remove class by ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Class removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
            @ApiResponse(responseCode = "404", description = "Class not found")})
    public ResponseEntity<Void> removeClass(@PathVariable Long id) {
        if(log.isInfoEnabled()) {
            log.info("Request to remove class with ID: " + id);
        }
        try {
            classroomService.removeClass(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}