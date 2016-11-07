package au.com.example.user;

import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.persistence.exceptions.CreateUserException;
import au.com.example.service.user.model.SpringUserDetail;
import au.com.example.spring.PersistenceConfig;
import au.com.example.spring.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SecurityConfig.class, PersistenceConfig.class })
public class UserDetailsManagerUnitTest {

    @Autowired
    @InjectMocks
    private UserDetailsManager userDetailsManager;

    @Mock
    private UserDAO mockUserDAO;

    @Mock
    private SpringUserDetail mockUserDetail;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldLoadUserWithEmailValid() {
        when(mockUserDAO.loadUser(anyString())).thenReturn(mockUserDetail);

        UserDetails user = userDetailsManager.loadUserByUsername("test-user-valid@tester.com.au");

        assertNotNull(user);
        assertEquals(mockUserDetail.getUsername(), user.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotLoadUserWithUsernameNotFoundExceptionThrown() {
        when(mockUserDAO.loadUser(anyString())).thenThrow(UsernameNotFoundException.class);

        UserDetails user = userDetailsManager.loadUserByUsername("test-user-not-found@tester.com.au");

        assertNull(user);
    }

    @Test
    public void shouldCreateUserWithUserDetailsValid() {
        doNothing().when(mockUserDAO).createUser(any(UserDetails.class));

        userDetailsManager.createUser(
                new SpringUserDetail("test-user-valid@tester.com.au", "password", "test", "user", "test user",
                        new ArrayList<GrantedAuthority>(), true, true, true, true));
    }

    @Test(expected = CreateUserException.class)
    public void shouldNotCreateUserWithCreateUserExceptionThrown() {
        doThrow(CreateUserException.class).when(mockUserDAO).createUser(any(UserDetails.class));

        userDetailsManager.createUser(
                new SpringUserDetail("test-user-valid@tester.com.au", "password", "test", "user", "test user",
                        new ArrayList<GrantedAuthority>(), true, true, true, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateUserWithIllegalArgumentExceptionThrown() {
        doThrow(IllegalArgumentException.class).when(mockUserDAO).createUser(any(UserDetails.class));

        userDetailsManager.createUser(new SpringUserDetail());
    }
}
