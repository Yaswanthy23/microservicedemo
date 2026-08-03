package com.crm.admin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.crm.admin.model.Team;
import com.crm.admin.model.User;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByTenantSegment(String tenantSegment);
    List<Team> findByManager(User manager);
}
