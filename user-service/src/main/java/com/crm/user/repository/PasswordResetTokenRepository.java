package com.crm.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.user.model.PasswordResetToken;
import com.crm.user.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	Optional<PasswordResetToken> findByToken(String token);
	Optional<PasswordResetToken> findByUser(User user);
	void deleteByUser(User user);


}
