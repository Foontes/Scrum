package com.backend.repositories;

import com.backend.models.User;
import com.backend.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findTopByUserAndEndAtIsNullOrderByStartAtDesc(User user);
}
