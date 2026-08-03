package com.crm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.user.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
