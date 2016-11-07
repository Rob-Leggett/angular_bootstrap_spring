package au.com.example.persistence.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws
            AuthenticationException {

        if (getUserDetailsService() instanceof UserDetailsManager) {
            // TODO: custom checks if you wish
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
