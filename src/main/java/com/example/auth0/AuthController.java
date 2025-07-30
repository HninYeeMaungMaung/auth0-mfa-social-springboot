package com.example.auth0;

import com.example.auth0.model.User;
import com.example.auth0.model.UserDevice;
import com.example.auth0.repository.UserDeviceRepository;
import com.example.auth0.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    public AuthController(UserRepository userRepository, UserDeviceRepository userDeviceRepository) {
        this.userRepository = userRepository;
        this.userDeviceRepository = userDeviceRepository;
    }

    @GetMapping("/")
    public String home() {
        return "<a href='/oauth2/authorization/auth0'>Login with Auth0</a>";
    }

    @GetMapping("/success")
    public String success(@AuthenticationPrincipal OidcUser user) {
        saveUserInformation(user);

        return "Login success for: " + user.getEmail();
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.clearContext();
        return "You have been logged out";
    }

    @GetMapping("/user-profile")
    public Map<String, Object> profile(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "email", jwt.getClaim("email"),
                "name", jwt.getClaim("name"),
                "issuer", jwt.getIssuer().toString()
        );
    }

    @GetMapping("/token")
    public String getIdToken(@AuthenticationPrincipal OidcUser principal) {
        return principal.getIdToken().getTokenValue(); // This is the raw ID token
    }

    @GetMapping("/forward")
    public ResponseEntity<String> forwardToken(@AuthenticationPrincipal OidcUser principal, OAuth2AuthenticationToken authentication) {
        System.out.println("ID token: " + principal.getIdToken().getTokenValue());
        saveUserInformation(principal);
        String accessToken = principal.getIdToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // SAFER way
        HttpEntity<?> request = new HttpEntity<>(headers);

        String url = "http://localhost:2020/api/hello?name=" + principal.getFullName();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    public void saveUserInformation(OidcUser user) {
        String email = user.getEmail();
        Optional<User> existing = userRepository.findByEmail(email);
        User u = existing.orElseGet(() -> new User(null, user.getFullName(), "N/A", "USER", email, "", "", ""));
        userRepository.save(u);

        UserDevice device = new UserDevice(null, u.getId(), "device-" + System.currentTimeMillis());
        userDeviceRepository.save(device);
    }

}
