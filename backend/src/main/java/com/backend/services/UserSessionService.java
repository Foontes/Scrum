package com.backend.services;

import com.backend.models.User;
import com.backend.models.UserSession;
import com.backend.repositories.UserRepository;
import com.backend.repositories.UserSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserSessionService {

    private final UserSessionRepository sessionRepo;
    private final UserRepository userRepo;

    public UserSessionService(UserSessionRepository sessionRepo, UserRepository userRepo) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
    }

    public List<UserSession> getAllSessions() {
        return sessionRepo.findAll();
    }

    public UserSession getById(Long id) {
        return sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));
    }

    public UserSession createSession(UUID userId, Instant startAt) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        UserSession session = new UserSession();
        session.setUser(user);
        session.setStartAt(startAt != null ? startAt : Instant.now());

        return sessionRepo.save(session);
    }

    public UserSession endSession(Long id, Instant endAt) {
        UserSession session = getById(id);
        session.setEndAt(endAt != null ? endAt : Instant.now());
        return sessionRepo.save(session);
    }

    public boolean deleteSession(Long id) {
        if (!sessionRepo.existsById(id)) {
            throw new RuntimeException("Session not found with id: " + id);
        }
        sessionRepo.deleteById(id);
        return true;
    }
}
