package au.com.example.service.user.model;

import java.io.Serializable;
import java.util.Collection;

public class UserDetail implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private String alias;
    private Collection<MembershipDetail> memberships;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    public UserDetail() { }

    public UserDetail(
            String email,
            String firstName,
            String lastName,
            String alias,
            Collection<MembershipDetail> memberships,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.memberships = memberships;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    public String getEmail() {
        return email;
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

    public Collection<MembershipDetail> getMemberships() {
        return memberships;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean getAccountNonLocked() {
        return accountNonLocked;
    }
}
