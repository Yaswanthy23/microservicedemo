package com.crm.admin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.crm.admin.model.DomainCategory;

public interface DomainCategoryRepository extends JpaRepository<DomainCategory, Long> {
    List<DomainCategory> findByTenantSegment(String tenantSegment);
}
