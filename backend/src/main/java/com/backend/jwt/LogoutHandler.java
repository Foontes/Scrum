package com.backend.jwt;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private LoginEventRepository loginRepo;
    @Autowired
    private UserSessionRepository sessionRepo;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        loginRepo.save(new LoginEvent(user, LoginEvent.EventType.LOGOUT));

        sessionRepo.findTopByUserAndEndAtIsNullOrderByStartAtDesc(user)
                   .ifPresent(session -> {
                       session.setEndAt(Instant.now());
                       sessionRepo.save(session);
                   });

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
