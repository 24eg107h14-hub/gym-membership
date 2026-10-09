package com.anurag.gymMemberShip.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "members")
public class Member {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @OneToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id", unique = true)
      private User user;

      @Column(nullable = false)
      private String fullName;

      @Column(nullable = false, unique = true)
      private String phone;

      @Column(unique = true)
      private String email;

      private String gender;

      private String address;

      private LocalDate dob;

      public Member() {
      }

      public Member(Long id, User user, String fullName, String phone, String email, String gender, String address, LocalDate dob) {
            this.id = id;
            this.user = user;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
            this.address = address;
            this.dob = dob;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public User getUser() {
            return user;
      }

      public void setUser(User user) {
            this.user = user;
      }

      public String getFullName() {
            return fullName;
      }

      public void setFullName(String fullName) {
            this.fullName = fullName;
      }

      public String getPhone() {
            return phone;
      }

      public void setPhone(String phone) {
            this.phone = phone;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
      }

      public String getGender() {
            return gender;
      }

      public void setGender(String gender) {
            this.gender = gender;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public LocalDate getDob() {
            return dob;
      }

      public void setDob(LocalDate dob) {
            this.dob = dob;
      }
}
