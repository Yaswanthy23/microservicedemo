package com.crm.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.user.model.Payslip;
import com.crm.user.model.User;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    List<Payslip> findByEmployeeOrderByIdDesc(User employee);

    List<Payslip> findByTenantSegmentOrderByIdDesc(String tenantSegment);

    Optional<Payslip> findByEmployeeAndPaymentMonthAndPaymentYear(User employee, Integer paymentMonth, Integer paymentYear);
}
