package au.com.example.service.user.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

public class SpringUserDetail extends User {
    private static final long serialVersionUID = 5148339247016106466L;

    private String firstName;
    private String lastName;
    private String alias;

    public SpringUserDetail() {
        this(null, null, null, null, null, new ArrayList<GrantedAuthority>(), false, false, false, false);
    }

    public SpringUserDetail(
            String email,
            String password,
            String firstName,
            String lastName,
            String alias,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked) {

        super(email, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);

        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAlias() {
        return alias;
    }
}
