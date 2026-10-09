package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.MemberDto;
import com.anurag.gymMemberShip.dto.MemberResponseDto;
import com.anurag.gymMemberShip.service.MemberService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:5173")
public class MemberController {

      @Autowired
      private MemberService memberService;

      @GetMapping
      public ResponseEntity<List<MemberResponseDto>> getAll(
                  @RequestParam(required = false) String search,
                  @RequestParam(required = false) String status) {
            return ResponseEntity.ok(memberService.getAll(search, status));
      }

      @GetMapping("/{id}")
      public ResponseEntity<MemberResponseDto> getById(@PathVariable Long id) {
            return ResponseEntity.ok(memberService.getById(id));
      }

      @PostMapping
      public ResponseEntity<MemberResponseDto> add(@Valid @RequestBody MemberDto dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(memberService.add(dto));
      }

      @PutMapping("/{id}")
      public ResponseEntity<MemberResponseDto> update(@PathVariable Long id, @Valid @RequestBody MemberDto dto) {
            return ResponseEntity.ok(memberService.update(id, dto));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            memberService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Member deleted successfully"));
      }

      @PostMapping("/{id}/renew")
      public ResponseEntity<MemberResponseDto> renew(
                  @PathVariable Long id,
                  @RequestParam(required = false) Long planId) {
            return ResponseEntity.ok(memberService.renew(id, planId));
      }
}
