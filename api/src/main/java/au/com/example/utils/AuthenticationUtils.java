package au.com.example.utils;

import au.com.example.service.user.model.MembershipDetail;
import au.com.example.service.user.model.SpringMembershipDetail;
import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.service.user.model.UserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public final class AuthenticationUtils {

    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = principal.toString();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        return username;
    }

    public static UserDetail toUserDetail(SpringUserDetail springUserDetail) {
        return (springUserDetail == null) ? null : new UserDetail(
                springUserDetail.getUsername(),
                springUserDetail.getFirstName(),
                springUserDetail.getLastName(),
                springUserDetail.getAlias(),
                toMembershipDetail(springUserDetail.getAuthorities()),
                springUserDetail.isEnabled(),
                springUserDetail.isAccountNonExpired(),
                springUserDetail.isCredentialsNonExpired(),
                springUserDetail.isAccountNonLocked());
    }

    // ========== HELPERS =========

    private static Collection<MembershipDetail> toMembershipDetail(Collection<? extends GrantedAuthority> grantedAuthorities) {
        Collection<MembershipDetail> membershipDetails = new ArrayList<>();

        for(GrantedAuthority authority : grantedAuthorities) {
            if(authority instanceof SpringMembershipDetail) {
                SpringMembershipDetail springmembershipDetail = (SpringMembershipDetail)authority;

                membershipDetails.add(new MembershipDetail(
                        springmembershipDetail.getId(),
                        springmembershipDetail.getType(),
                        springmembershipDetail.getExpire()));
            }
        }

        return membershipDetails;
    }
}
