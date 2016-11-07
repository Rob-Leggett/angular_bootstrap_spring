package au.com.speak.persistence.dao.user;

import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.spring.PersistenceConfig;
import au.com.example.spring.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SecurityConfig.class, PersistenceConfig.class })
public class UserDaoIntegrationTest {

    @Autowired
    private UserDAO userDao;

    @Test
    public void shouldCreateUserSuccessfully() {
        userDao.createUser(new SpringUserDetail("test_user1@tester.com.au", "password", "test", "user", "test user updated", new ArrayList<GrantedAuthority>(), true, true, true, true));

        assertTrue(userDao.userExists("test_user1@tester.com.au"));
    }

    @Test
    public void shouldUpdateUserSuccessfully() {

        SpringUserDetail before = (SpringUserDetail)userDao.loadUser("test-user-db@tester.com.au");

        assertEquals("Test User", before.getAlias());

        userDao.updateUser(
                new SpringUserDetail("test-user-db@tester.com.au", "$2a$10$xT/t.6abkjaRpAkNOrt43OD9Cn2aaS3vgxQsnLtEN7mOi6RpACvbm",
                        "Test", "User", "Test User Updated", new ArrayList<GrantedAuthority>(), true, true, true, true));

        SpringUserDetail after = (SpringUserDetail)userDao.loadUser("test-user-db@tester.com.au");

        assertEquals("Test User Updated", after.getAlias());
    }
}
