package au.com.example.service.authentication;

import au.com.example.constant.Constants;
import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.service.user.model.UserDetail;
import au.com.example.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;

@Service(Constants.SERVICE_TOKEN_AUTH)
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final TokenHandler tokenHandler;
    private final UserDAO userDao;

    public TokenAuthenticationServiceImpl(@Value("${app.token.secret}") String tokenSecret,
                                          @Lazy UserDAO userDao) {
        this.tokenHandler = new TokenHandler(Base64.getDecoder().decode(tokenSecret));
        this.userDao = userDao;
    }

    @Override
    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        final SpringUserDetail user = (SpringUserDetail)authentication.getPrincipal();
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(AuthenticationUtils.toUserDetail(user)));
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            if (token != null) {
                final UserDetail user = tokenHandler.parseUserFromToken(token);
                if (user != null) {
                    UserDetails details = userDao.loadUser(user.getEmail());
                    authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                }
            }
        }

        return authentication;
    }
}