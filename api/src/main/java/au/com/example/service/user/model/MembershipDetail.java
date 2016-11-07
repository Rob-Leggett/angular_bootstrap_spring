package au.com.example.service.user.model;

import au.com.example.persistence.dao.user.entity.MembershipType;

import java.io.Serializable;
import java.util.Date;

public class MembershipDetail implements Serializable {

    private Long id;
    private MembershipType type;
    private Date expire;

    public MembershipDetail() {
    }

    public MembershipDetail(Long id, MembershipType type, Date expire) {
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
}
