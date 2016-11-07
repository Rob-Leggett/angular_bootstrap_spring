package au.com.example.service.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenAuthenticationService {

    void addAuthentication(HttpServletResponse response, Authentication authentication);

    Authentication getAuthentication(HttpServletRequest request);
}
