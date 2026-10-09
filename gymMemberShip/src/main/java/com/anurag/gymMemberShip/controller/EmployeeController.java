package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.EmployeeDto;
import com.anurag.gymMemberShip.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

      @Autowired
      private EmployeeService employeeService;

      @GetMapping
      public ResponseEntity<List<EmployeeDto>> list() {
            return ResponseEntity.ok(employeeService.list());
      }

      @PostMapping
      public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(dto));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            employeeService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Employee deleted successfully"));
      }
}
