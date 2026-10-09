package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.CouponDto;
import com.anurag.gymMemberShip.dto.CouponValidateDto;
import com.anurag.gymMemberShip.entity.Coupon;
import com.anurag.gymMemberShip.enums.DiscountType;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.CouponRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

      @Autowired
      private CouponRepository couponRepository;

      public List<CouponDto> getAll() {
            return couponRepository.findAll().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public List<CouponDto> getActiveCoupons() {
            return couponRepository.findByActive(true).stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public CouponDto getById(Long id) {
            Coupon coupon = couponRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
            return mapToDto(coupon);
      }

      public CouponDto create(CouponDto dto) {
            if (couponRepository.existsByCodeIgnoreCase(dto.getCode().trim())) {
                  throw new IllegalArgumentException("Coupon code already exists: " + dto.getCode());
            }

            Coupon coupon = new Coupon();
            coupon.setCode(dto.getCode().trim().toUpperCase());
            coupon.setDescription(dto.getDescription());
            coupon.setDiscountType(dto.getDiscountType() != null ? dto.getDiscountType() : DiscountType.PERCENTAGE);
            coupon.setDiscountValue(dto.getDiscountValue());
            coupon.setActive(dto.isActive());
            coupon.setStartDate(dto.getStartDate());
            coupon.setEndDate(dto.getEndDate());

            Coupon saved = couponRepository.save(coupon);
            return mapToDto(saved);
      }

      public CouponDto update(Long id, CouponDto dto) {
            Coupon coupon = couponRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

            coupon.setCode(dto.getCode().trim().toUpperCase());
            coupon.setDescription(dto.getDescription());
            coupon.setDiscountType(dto.getDiscountType());
            coupon.setDiscountValue(dto.getDiscountValue());
            coupon.setActive(dto.isActive());
            coupon.setStartDate(dto.getStartDate());
            coupon.setEndDate(dto.getEndDate());

            Coupon updated = couponRepository.save(coupon);
            return mapToDto(updated);
      }

      public CouponDto toggleStatus(Long id) {
            Coupon coupon = couponRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

            coupon.setActive(!coupon.isActive());
            Coupon updated = couponRepository.save(coupon);
            return mapToDto(updated);
      }

      public void delete(Long id) {
            if (!couponRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Coupon not found with id: " + id);
            }
            couponRepository.deleteById(id);
      }

      public CouponValidateDto.Response validate(CouponValidateDto.Request req) {
            String code = req.getCode() != null ? req.getCode().trim() : "";
            BigDecimal planPrice = req.getPlanPrice() != null ? req.getPlanPrice() : BigDecimal.ZERO;

            if (code.isEmpty()) {
                  return new CouponValidateDto.Response(false, "Coupon code cannot be empty", code, null, BigDecimal.ZERO, BigDecimal.ZERO, planPrice);
            }

            Coupon coupon = couponRepository.findByCodeIgnoreCase(code).orElse(null);
            if (coupon == null) {
                  return new CouponValidateDto.Response(false, "Invalid coupon code", code, null, BigDecimal.ZERO, BigDecimal.ZERO, planPrice);
            }

            if (!coupon.isActive()) {
                  return new CouponValidateDto.Response(false, "This coupon is inactive", code, null, BigDecimal.ZERO, BigDecimal.ZERO, planPrice);
            }

            LocalDate today = LocalDate.now();
            if (coupon.getStartDate() != null && today.isBefore(coupon.getStartDate())) {
                  return new CouponValidateDto.Response(false, "Coupon is not yet valid", code, null, BigDecimal.ZERO, BigDecimal.ZERO, planPrice);
            }
            if (coupon.getEndDate() != null && today.isAfter(coupon.getEndDate())) {
                  return new CouponValidateDto.Response(false, "Coupon has expired", code, null, BigDecimal.ZERO, BigDecimal.ZERO, planPrice);
            }

            BigDecimal discountAmount = BigDecimal.ZERO;
            if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
                  BigDecimal percentage = coupon.getDiscountValue().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                  discountAmount = planPrice.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
            } else {
                  discountAmount = coupon.getDiscountValue().setScale(2, RoundingMode.HALF_UP);
            }

            // Cap discount to plan price
            if (discountAmount.compareTo(planPrice) > 0) {
                  discountAmount = planPrice;
            }

            BigDecimal finalAmount = planPrice.subtract(discountAmount);
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                  finalAmount = BigDecimal.ZERO;
            }

            return new CouponValidateDto.Response(
                        true,
                        "Coupon applied successfully!",
                        coupon.getCode(),
                        coupon.getDiscountType(),
                        coupon.getDiscountValue(),
                        discountAmount,
                        finalAmount
            );
      }

      public CouponDto mapToDto(Coupon coupon) {
            return new CouponDto(
                        coupon.getId(),
                        coupon.getCode(),
                        coupon.getDescription(),
                        coupon.getDiscountType(),
                        coupon.getDiscountValue(),
                        coupon.isActive(),
                        coupon.getStartDate(),
                        coupon.getEndDate()
            );
      }
}
