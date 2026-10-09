package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

      Optional<Member> findByEmail(String email);

      Optional<Member> findByUserId(Long userId);

      boolean existsByPhone(String phone);

      boolean existsByPhoneAndIdNot(String phone, Long id);

      boolean existsByEmail(String email);

      boolean existsByEmailAndIdNot(String email, Long id);

      @Query("SELECT m FROM Member m WHERE " +
             "LOWER(m.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
             "LOWER(m.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
             "LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%'))")
      List<Member> search(@Param("query") String query);
}
