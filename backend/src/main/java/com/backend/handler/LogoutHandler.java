package com.backend.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.models.User;
import com.backend.models.LoginEvent;
import com.backend.repositories.UserRepository;
import com.backend.repositories.LoginEventRepository;
import com.backend.repositories.UserSessionRepository;

import java.io.IOException;
import java.time.Instant;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginEventRepository loginEventRepository;

    @Autowired
    private UserSessionRepository sessionRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        loginEventRepository.save(new LoginEvent(user, LoginEvent.EventType.LOGOUT));

        sessionRepository.findTopByUserAndEndAtIsNullOrderByStartAtDesc(user)
                         .ifPresent(session -> {
                             session.setEndAt(Instant.now());
                             sessionRepository.save(session);
                         });

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
