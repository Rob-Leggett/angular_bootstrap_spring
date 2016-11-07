package au.com.example.service.authentication;

import au.com.example.constant.Constants;
import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.service.user.model.UserDetail;
import au.com.example.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

@Service(Constants.SERVICE_TOKEN_AUTH)
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final TokenHandler tokenHandler;

    @Autowired
    private UserDAO userDao;

    public TokenAuthenticationServiceImpl() {
        // TODO: parse this as a property
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary("9SyECk96oDsTmXfogIfgdjhdsgvagHJLKNLvfdsfR8cbXTvoPjX+Pq/T/b1PqpHX0lYm0oCBjXWICA=="));
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