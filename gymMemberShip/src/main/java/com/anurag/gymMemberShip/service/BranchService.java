package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.BranchDto;
import com.anurag.gymMemberShip.entity.Branch;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.BranchRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService {

      @Autowired
      private BranchRepository branchRepository;

      public List<BranchDto> getAll() {
            return branchRepository.findAll().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public List<BranchDto> getActiveBranches() {
            return branchRepository.findByActive(true).stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public BranchDto getById(Long id) {
            Branch branch = branchRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
            return mapToDto(branch);
      }

      public BranchDto create(BranchDto dto) {
            Branch branch = new Branch();
            branch.setName(dto.getName());
            branch.setAddress(dto.getAddress());
            branch.setCity(dto.getCity());
            branch.setState(dto.getState());
            branch.setPhone(dto.getPhone());
            branch.setEmail(dto.getEmail());
            branch.setActive(dto.isActive());

            Branch saved = branchRepository.save(branch);
            return mapToDto(saved);
      }

      public BranchDto update(Long id, BranchDto dto) {
            Branch branch = branchRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

            branch.setName(dto.getName());
            branch.setAddress(dto.getAddress());
            branch.setCity(dto.getCity());
            branch.setState(dto.getState());
            branch.setPhone(dto.getPhone());
            branch.setEmail(dto.getEmail());
            branch.setActive(dto.isActive());

            Branch updated = branchRepository.save(branch);
            return mapToDto(updated);
      }

      public BranchDto toggleStatus(Long id) {
            Branch branch = branchRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

            branch.setActive(!branch.isActive());
            Branch updated = branchRepository.save(branch);
            return mapToDto(updated);
      }

      public void delete(Long id) {
            if (!branchRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Branch not found with id: " + id);
            }
            branchRepository.deleteById(id);
      }

      public BranchDto mapToDto(Branch branch) {
            return new BranchDto(
                        branch.getId(),
                        branch.getName(),
                        branch.getAddress(),
                        branch.getCity(),
                        branch.getState(),
                        branch.getPhone(),
                        branch.getEmail(),
                        branch.isActive()
            );
      }
}
