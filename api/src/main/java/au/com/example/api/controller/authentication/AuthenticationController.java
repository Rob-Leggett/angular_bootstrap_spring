package au.com.example.api.controller.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public void login() { }

    @RequestMapping(value = "/logout/validate", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public void logout(@RequestParam(value = "status") String status) {
        if(log.isDebugEnabled()) {
            log.debug("Logout status " + status);
        }
    }
}
