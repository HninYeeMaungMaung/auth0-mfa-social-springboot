package com.example.auth0;

import com.example.auth0.model.User;
import com.example.auth0.model.UserDevice;
import com.example.auth0.repository.UserDeviceRepository;
import com.example.auth0.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;

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
        String email = user.getEmail();
        Optional<User> existing = userRepository.findByEmail(email);
        User u = existing.orElseGet(() -> new User(null, user.getFullName(), "N/A", "USER", email, "", "", ""));
        userRepository.save(u);

        UserDevice device = new UserDevice(null, u.getId(), "device-" + System.currentTimeMillis());
        userDeviceRepository.save(device);

        return "Login success for: " + email;
    }

    @PostMapping("/logout")
    public String logout() {

        return "You have been logged out";
    }
}
