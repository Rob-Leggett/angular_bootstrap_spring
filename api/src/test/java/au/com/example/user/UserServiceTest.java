package au.com.example.user;

import au.com.example.Application;
import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.service.user.UserService;
import au.com.example.service.user.model.SpringUserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserDAO mockUserDAO;

    private SpringUserDetail mockUserDetail;

    @BeforeEach
    public void setup() {
        mockUserDetail = new SpringUserDetail(
                "test-user-valid@tester.com.au", "password", "test", "user", "test user",
                new ArrayList<GrantedAuthority>(), true, true, true, true);
    }

    @Test
    public void shouldLoadUserWithEmailValid() {
        when(mockUserDAO.loadUser(anyString())).thenReturn(mockUserDetail);

        UserDetails user = userService.loadUserByUsername("test-user-valid@tester.com.au");

        assertNotNull(user);
        assertEquals(mockUserDetail.getUsername(), user.getUsername());
    }

    @Test
    public void shouldNotLoadUserWithUsernameNotFoundExceptionThrown() {
        when(mockUserDAO.loadUser(anyString())).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("test-user-not-found@tester.com.au");
        });
    }
}
