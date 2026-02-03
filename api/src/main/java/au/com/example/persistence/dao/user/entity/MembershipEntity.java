package au.com.example.persistence.dao.user.entity;

import au.com.example.constant.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = Constants.TABLE_MEMBERSHIP)
public class MembershipEntity implements Cloneable {
    private Long id;
    private MembershipType type;
    private Date expire;

    public MembershipEntity() {
        this(null, null, null);
    }

    public MembershipEntity(Long id, MembershipType type, Date expire) {
        this.id = id;
        this.type = type;
        this.expire = expire;
    }

    @Id
    @GeneratedValue(generator = "MembershipSeq")
    @SequenceGenerator(name = "MembershipSeq", sequenceName = "MEMBERSHIP_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "membership_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public MembershipType getType() {
        return type;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    @Column(name = "expire")
    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    // === Cloneable implementation

    @Override
    public MembershipEntity clone() {
        return new MembershipEntity(id, type, expire);
    }

    // === Overrides

    @Override
    public String toString() {
        return "MembershipEntity [id=" + id + ", type=" + type + ", expire=" + expire + "]";
    }
}
