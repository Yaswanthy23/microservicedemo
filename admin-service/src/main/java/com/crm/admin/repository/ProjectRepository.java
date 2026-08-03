package com.crm.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crm.admin.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
