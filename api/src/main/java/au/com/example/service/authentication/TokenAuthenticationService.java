package au.com.example.service.authentication;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenAuthenticationService {

    void addAuthentication(HttpServletResponse response, Authentication authentication);

    Authentication getAuthentication(HttpServletRequest request);
}
