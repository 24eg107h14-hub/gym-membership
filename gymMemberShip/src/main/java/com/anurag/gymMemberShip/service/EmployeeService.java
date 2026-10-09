package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.EmployeeDto;
import com.anurag.gymMemberShip.entity.User;
import com.anurag.gymMemberShip.enums.Role;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private PasswordEncoder passwordEncoder;

      public List<EmployeeDto> list() {
            return userRepository.findByRole(Role.SUPPORT).stream()
                        .map(user -> new EmployeeDto(user.getId(), user.getName(), user.getEmail()))
                        .collect(Collectors.toList());
      }

      public EmployeeDto create(EmployeeDto dto) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                  throw new IllegalArgumentException("Email is already in use");
            }
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                  throw new IllegalArgumentException("Password is required");
            }

            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(Role.SUPPORT);

            User saved = userRepository.save(user);
            return new EmployeeDto(saved.getId(), saved.getName(), saved.getEmail());
      }

      public void delete(Long id) {
            User user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

            if (user.getRole() != Role.SUPPORT) {
                  throw new IllegalArgumentException("Cannot delete non-support user via this endpoint");
            }

            userRepository.delete(user);
      }
}
