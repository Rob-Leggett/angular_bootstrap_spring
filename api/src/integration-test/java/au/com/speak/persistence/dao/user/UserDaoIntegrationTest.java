package au.com.speak.persistence.dao.user;

import au.com.example.Application;
import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.service.user.model.SpringUserDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class)
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

        SpringUserDetail before = (SpringUserDetail)userDao.loadUser("user@tester.com.au");

        assertEquals("Test User", before.getAlias());

        userDao.updateUser(
                new SpringUserDetail("user@tester.com.au", "$2a$10$xT/t.6abkjaRpAkNOrt43OD9Cn2aaS3vgxQsnLtEN7mOi6RpACvbm",
                        "Test", "User", "Test User Updated", new ArrayList<GrantedAuthority>(), true, true, true, true));

        SpringUserDetail after = (SpringUserDetail)userDao.loadUser("user@tester.com.au");

        assertEquals("Test User Updated", after.getAlias());
    }
}
