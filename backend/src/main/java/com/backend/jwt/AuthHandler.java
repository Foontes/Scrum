package com.backend.jwt;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LoginEventRepository loginRepo;
    
    @Autowired
    private UserSessionRepository sessionRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        /* Grava evento LOGIN */
        loginRepo.save(new LoginEvent(user, LoginEvent.EventType.LOGIN));

        /* Inicia sessão */
        sessionRepo.save(new UserSession(user, Instant.now()));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
