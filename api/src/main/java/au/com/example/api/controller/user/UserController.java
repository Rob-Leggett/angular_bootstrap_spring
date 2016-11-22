package au.com.example.api.controller.user;

import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.service.user.model.UserDetail;
import au.com.example.utils.AuthenticationUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserDetail retrieveUser(@AuthenticationPrincipal SpringUserDetail user) {
        return AuthenticationUtils.toUserDetail(user);
    }
}
