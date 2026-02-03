package au.com.example.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void changePassword(String email, String password);
    boolean userExists(String email);
}
