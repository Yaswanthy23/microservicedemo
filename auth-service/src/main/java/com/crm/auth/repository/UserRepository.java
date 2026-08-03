package com.crm.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crm.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username ORDER BY u.id ASC LIMIT 1")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email ORDER BY u.id ASC LIMIT 1")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email ORDER BY u.id ASC LIMIT 1")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username OR u.email = :email")
    boolean existsByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:tenantSegment% ORDER BY u.id DESC")
    List<User> findByTenantSegment(@Param("tenantSegment") String tenantSegment);
}
