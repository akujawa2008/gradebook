package com.britenet.gradebook.users;

import com.britenet.gradebook.exception.ResourceNotFoundException;
import com.britenet.gradebook.teacherAssignment.TeacherAssignment;
import com.britenet.gradebook.teacherAssignment.TeacherAssignmentRequest;
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
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/admin/addStudent")
    @Operation(summary = "Add student", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add student succeeded"),
            @ApiResponse(responseCode = "400", description = "Add student failed")})
    public ResponseEntity<Users> addStudent(@RequestBody UsersRequestDto studentRequestDto) {
        if (log.isInfoEnabled()) {
            log.info("Request to create user with role 'STUDENT':" + studentRequestDto);
        }

        try {
            Users savedStudent = usersService.addStudent(studentRequestDto);
            return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/addTeacher")
    @Operation(summary = "Add teacher", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add teacher succeeded"),
            @ApiResponse(responseCode = "400", description = "Add teacher failed")})
    public ResponseEntity<Users> addTeacher(@RequestBody UsersRequestDto teacherRequestDto) {
        if (log.isInfoEnabled()) {
            log.info("Request to create user with role 'TEACHER':" + teacherRequestDto);
        }

        try {
            Users savedTeacher = usersService.addTeacher(teacherRequestDto);
            return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/assignTeacherToSubject")
    @Operation(summary = "Assign teacher to subject", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assign teacher to subject succeeded"),
            @ApiResponse(responseCode = "400", description = "Assign teacher to subject failed")})
    public ResponseEntity<TeacherAssignment> assignTeacherToSubject(@RequestBody TeacherAssignmentRequest request) {
        if(log.isInfoEnabled()) {
            log.info("Request to assign teacher to subject: " + request);
        }
        try {
            usersService.assignTeacherToSubject(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/removeUser/{id}")
    @Operation(summary = "Remove user by ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        if (log.isInfoEnabled()) {
            log.info("Request to remove user with ID: " + id);
        }

        try {
            usersService.removeUser(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


