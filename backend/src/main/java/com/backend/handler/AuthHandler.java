package com.backend.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.models.User;
import com.backend.models.LoginEvent;
import com.backend.models.UserSession;
import com.backend.repositories.UserRepository;
import com.backend.repositories.LoginEventRepository;
import com.backend.repositories.UserSessionRepository;

import java.io.IOException;
import java.time.Instant;

@Component
public class AuthHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LoginEventRepository loginRepo;

    @Autowired
    private UserSessionRepository sessionRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        loginRepo.save(new LoginEvent(user, LoginEvent.EventType.LOGIN));
        sessionRepo.save(new UserSession(user, Instant.now()));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
