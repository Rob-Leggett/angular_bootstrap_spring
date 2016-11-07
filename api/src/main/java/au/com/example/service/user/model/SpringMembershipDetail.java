package au.com.example.service.user.model;


import au.com.example.persistence.dao.user.entity.MembershipType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

public class SpringMembershipDetail implements GrantedAuthority {

    private Long id;
    private MembershipType type;
    private Date expire;

    public SpringMembershipDetail(Long id, MembershipType type, Date expire) {
        this.id = id;
        this.type = type;
        this.expire = expire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MembershipType getType() {
        return type;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    // ========== Implementation ==============

    @Override public String getAuthority() {
        return (type == null) ? null : type.name();
    }
}
