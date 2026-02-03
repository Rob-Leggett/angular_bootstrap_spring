package au.com.example.persistence.dao.user;

import au.com.example.persistence.dao.base.BaseDao;
import au.com.example.persistence.dao.user.entity.MembershipEntity;
import au.com.example.persistence.dao.user.entity.UserEntity;
import au.com.example.persistence.dao.user.query.SelectUser;
import au.com.example.persistence.dao.user.query.UpdatePassword;
import au.com.example.persistence.exceptions.ChangePasswordException;
import au.com.example.persistence.exceptions.CreateUserException;
import au.com.example.persistence.exceptions.DeleteUserException;
import au.com.example.persistence.exceptions.UpdateUserException;
import au.com.example.service.user.model.SpringMembershipDetail;
import au.com.example.service.user.model.SpringUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDAOImpl extends BaseDao implements UserDAO {
    private static Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUser(String email)
            throws UsernameNotFoundException, DataAccessException {
        UserEntity userEntity = loadDataSingle(UserEntity.class, new SelectUser(email));

        if (userEntity == null) {
            throw new UsernameNotFoundException("User " + email + " could not be found.");
        } else {
            return toUserDetails(userEntity);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void changePassword(String username, String password) throws ChangePasswordException {
        try {
            String encodedPassword = passwordEncoder.encode(password);

            int rowsUpdated = updateDeleteDataSingle(new UpdatePassword(username, encodedPassword));

            log.debug("Number of rows updated: " + rowsUpdated);
        } catch (Exception e) {
            log.error("Unable to change password for user: " + username + " because " + e.getMessage());

            throw new ChangePasswordException("Unable to change password: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String email) {
        boolean userExists = false;

        try {
            UserDetails user = loadUser(email);

            if (user != null) {
                userExists = true;
            }
        } catch (Exception e) {
            log.error("Unable to determine if user " + email + " exists: " + e.getMessage());
        }

        return userExists;
    }

    @Override
    @Transactional(readOnly = false)
    public void createUser(UserDetails user) throws CreateUserException {
        EntityManager entityManager = getEmf().createEntityManager();

        try {
            EntityTransaction tx = null;

            try {
                tx = entityManager.getTransaction();

                tx.begin();

                entityManager.persist(toUserEntity(user, true));

                tx.commit();
            } catch (Exception e) {
                log.error("Exception during creating user " + user + ": " + e.getMessage());

                throw new CreateUserException(e.getMessage());
            } finally {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUser(UserDetails user) throws UpdateUserException {
        EntityManager entityManager = getEmf().createEntityManager();

        try {
            EntityTransaction tx = null;

            try {
                tx = entityManager.getTransaction();

                tx.begin();

                entityManager.merge(toUserEntity(user, false));

                tx.commit();
            } catch (Exception e) {
                log.error("Exception during updating user " + user + ": " + e.getMessage());

                throw new UpdateUserException(e.getMessage());
            } finally {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(String email) throws DeleteUserException {
        EntityManager entityManager = getEmf().createEntityManager();

        try {
            EntityTransaction tx = null;

            try {
                tx = entityManager.getTransaction();

                tx.begin();

                UserEntity userEntity = loadDataSingle(UserEntity.class, new SelectUser(email));

                if (userEntity == null) {
                    log.error("User with username " + email + " not found");

                    throw new DeleteUserException("User with username " + email + " not found");
                }

                entityManager.remove(entityManager.find(UserEntity.class, userEntity.getEmail()));

                tx.commit();
            } catch (Exception e) {
                log.error("Exception during deleting user " + email + ": " + e.getMessage());

                throw new DeleteUserException(e.getMessage());
            } finally {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            }
        } finally {
            entityManager.close();
        }
    }

    // === Helpers

    // === Service to Entity entity

    private UserEntity toUserEntity(UserDetails userDetails, boolean encodePassword) {
        UserEntity userEntity = null;

        if (userDetails != null) {
            if (userDetails instanceof SpringUserDetail) {
                SpringUserDetail user = (SpringUserDetail) userDetails;

                userEntity = new UserEntity(
                        user.getUsername(),
                        encodePassword ? passwordEncoder.encode(user.getPassword()) : user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAlias(),
                        getSecurityRoles(user.getAuthorities()),
                        user.isEnabled(),
                        user.isAccountNonExpired(),
                        user.isCredentialsNonExpired(),
                        user.isAccountNonLocked());
            } else {
                log.error("Only supports " + SpringUserDetail.class + " all other classes unsupported");
            }
        }

        return userEntity;
    }

    private List<MembershipEntity> getSecurityRoles(Collection<GrantedAuthority> authorities) {
        List<MembershipEntity> securityRoles = new ArrayList<>();

        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority instanceof SpringMembershipDetail) {
                    SpringMembershipDetail membership = (SpringMembershipDetail) authority;

                    securityRoles.add(new MembershipEntity(membership.getId(), membership.getType(), membership.getExpire()));
                }
            }
        }

        return securityRoles;
    }

    // === Entity to Service entity

    private UserDetails toUserDetails(UserEntity userEntity) {
        UserDetails userDetails = null;

        if (userEntity != null) {
            userDetails = new SpringUserDetail(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getAlias(),
                    getGrantedAuthorities(userEntity.getMemberships()),
                    userEntity.isEnabled(),
                    userEntity.isAccountNonExpired(),
                    userEntity.isCredentialsNonExpired(),
                    userEntity.isAccountNonLocked());
        }

        return userDetails;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(List<MembershipEntity> securityRoles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (securityRoles != null) {
            for (MembershipEntity role : securityRoles) {
                authorities.add(new SpringMembershipDetail(role.getId(), role.getType(), role.getExpire()));
            }
        }

        return authorities;
    }
}
