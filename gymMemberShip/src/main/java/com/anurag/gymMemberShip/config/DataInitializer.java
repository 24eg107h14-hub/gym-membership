package com.anurag.gymMemberShip.config;

import com.anurag.gymMemberShip.entity.Branch;
import com.anurag.gymMemberShip.entity.Coupon;
import com.anurag.gymMemberShip.entity.MembershipPlan;
import com.anurag.gymMemberShip.entity.User;
import com.anurag.gymMemberShip.enums.DiscountType;
import com.anurag.gymMemberShip.enums.PlanStatus;
import com.anurag.gymMemberShip.enums.Role;
import com.anurag.gymMemberShip.repository.BranchRepository;
import com.anurag.gymMemberShip.repository.CouponRepository;
import com.anurag.gymMemberShip.repository.MembershipPlanRepository;
import com.anurag.gymMemberShip.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private MembershipPlanRepository planRepository;

      @Autowired
      private BranchRepository branchRepository;

      @Autowired
      private CouponRepository couponRepository;

      @Autowired
      private PasswordEncoder passwordEncoder;

      /** Demo branch network across India: name, address, city, state, phone, email. */
      private static final String[][] INDIA_BRANCHES = {
                  { "Downtown Fitness Hub", "Shop 12, Linking Road, Bandra West", "Mumbai", "Maharashtra", "+91 98200 11223", "bandra@gymmanager.in" },
                  { "Westside Athletic Club", "4th Floor, Phoenix Marketcity, Viman Nagar", "Pune", "Maharashtra", "+91 98200 11224", "viman@gymmanager.in" },
                  { "Uptown Power Gym", "Plot 27, Sector 18 Market", "Noida", "Uttar Pradesh", "+91 98200 11225", "noida@gymmanager.in" },
                  { "Capital Fitness Studio", "B-14, Connaught Place, Inner Circle", "New Delhi", "Delhi", "+91 98200 11226", "cp@gymmanager.in" },
                  { "Indiranagar Strength Lab", "220, 100 Feet Road, Indiranagar", "Bengaluru", "Karnataka", "+91 98200 11227", "indiranagar@gymmanager.in" },
                  { "Hitec City Iron Works", "Ground Floor, Cyber Towers, Hitec City", "Hyderabad", "Telangana", "+91 98200 11228", "hitec@gymmanager.in" },
                  { "Salt Lake Sector V Gym", "DN 51, Sector V, Salt Lake", "Kolkata", "West Bengal", "+91 98200 11229", "saltlake@gymmanager.in" },
                  { "T Nagar Wellness Centre", "88, Usman Road, T Nagar", "Chennai", "Tamil Nadu", "+91 98200 11230", "tnagar@gymmanager.in" },
                  { "Prahlad Nagar Fitness Club", "301, Iscon Emporio, Prahlad Nagar", "Ahmedabad", "Gujarat", "+91 98200 11231", "prahladnagar@gymmanager.in" },
                  { "Pink City Powerhouse", "6, Malviya Nagar, JLN Marg", "Jaipur", "Rajasthan", "+91 98200 11232", "malviyanagar@gymmanager.in" },
      };

      /** States used by the original demo seed data (pre India migration). */
      private static final Set<String> LEGACY_SEED_STATES = Set.of("NY", "CA", "TX");

      @Override
      public void run(String... args) throws Exception {
            if (userRepository.findByEmail("admin@gym.com").isEmpty()) {
                  User admin = new User();
                  admin.setName("Admin Manager");
                  admin.setEmail("admin@gym.com");
                  admin.setPassword(passwordEncoder.encode("admin123"));
                  admin.setRole(Role.ADMIN);
                  userRepository.save(admin);
            }

            if (userRepository.findByEmail("support@gym.com").isEmpty()) {
                  User support = new User();
                  support.setName("Support Staff");
                  support.setEmail("support@gym.com");
                  support.setPassword(passwordEncoder.encode("support123"));
                  support.setRole(Role.SUPPORT);
                  userRepository.save(support);
            }

            if (branchRepository.count() == 0) {
                  seedIndiaBranches();
            } else {
                  migrateLegacyBranchesToIndia();
            }

            if (planRepository.count() == 0) {
                  MembershipPlan monthly = new MembershipPlan();
                  monthly.setName("Monthly");
                  monthly.setDurationMonths(1);
                  monthly.setPrice(new BigDecimal("1000"));
                  monthly.setDescription("1 Month standard gym access");
                  monthly.setBenefits("Gym floor access, Locker room, 1 Fitness consultation");
                  monthly.setStatus(PlanStatus.ACTIVE);
                  planRepository.save(monthly);

                  MembershipPlan quarterly = new MembershipPlan();
                  quarterly.setName("Quarterly");
                  quarterly.setDurationMonths(3);
                  quarterly.setPrice(new BigDecimal("2700"));
                  quarterly.setDescription("3 Months value pack");
                  quarterly.setBenefits("Full gym access, Locker room, Steam bath access, 2 Fitness consultations");
                  quarterly.setStatus(PlanStatus.ACTIVE);
                  planRepository.save(quarterly);

                  MembershipPlan yearly = new MembershipPlan();
                  yearly.setName("Yearly VIP");
                  yearly.setDurationMonths(12);
                  yearly.setPrice(new BigDecimal("9000"));
                  yearly.setDescription("12 Months full VIP gym membership");
                  yearly.setBenefits("24/7 Access, All classes included, Locker & Sauna, Diet plan, Monthly body analysis");
                  yearly.setStatus(PlanStatus.ACTIVE);
                  planRepository.save(yearly);
            }

            if (couponRepository.count() == 0) {
                  Coupon c1 = new Coupon(null, "WELCOME10", "10% off for new members", DiscountType.PERCENTAGE, new BigDecimal("10"), true, LocalDate.now().minusDays(30), LocalDate.now().plusYears(1));
                  Coupon c2 = new Coupon(null, "FLAT500", "Flat Rs. 500 off on any plan", DiscountType.FIXED, new BigDecimal("500"), true, LocalDate.now().minusDays(30), LocalDate.now().plusYears(1));
                  couponRepository.save(c1);
                  couponRepository.save(c2);
            }
      }

      private void seedIndiaBranches() {
            for (String[] data : INDIA_BRANCHES) {
                  branchRepository.save(new Branch(null, data[0], data[1], data[2], data[3], data[4], data[5], true));
            }
      }

      /**
       * Moves the original demo branches (US cities) onto the India network and
       * adds any missing Indian branch. Only rows that still carry the untouched
       * legacy seed values are rewritten, so customised data is left alone.
       */
      private void migrateLegacyBranchesToIndia() {
            List<Branch> existing = branchRepository.findAll();
            boolean legacyFound = false;

            for (Branch branch : existing) {
                  if (branch.getState() == null || !LEGACY_SEED_STATES.contains(branch.getState().trim())) {
                        continue;
                  }
                  for (String[] data : INDIA_BRANCHES) {
                        if (!data[0].equals(branch.getName())) {
                              continue;
                        }
                        branch.setAddress(data[1]);
                        branch.setCity(data[2]);
                        branch.setState(data[3]);
                        branch.setPhone(data[4]);
                        branch.setEmail(data[5]);
                        branchRepository.save(branch);
                        legacyFound = true;
                        break;
                  }
            }

            if (!legacyFound) {
                  return;
            }

            Set<String> names = existing.stream().map(Branch::getName).collect(Collectors.toSet());
            Arrays.stream(INDIA_BRANCHES)
                        .filter(data -> !names.contains(data[0]))
                        .forEach(data -> branchRepository.save(
                                    new Branch(null, data[0], data[1], data[2], data[3], data[4], data[5], true)));
      }
}
