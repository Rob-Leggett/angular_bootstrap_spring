package au.com.example.user;

import au.com.example.spring.AppConfig;
import au.com.example.spring.PersistenceConfig;
import au.com.example.spring.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, SecurityConfig.class, PersistenceConfig.class})
@WebAppConfiguration
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class UserControllerEndpointUnitTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void shouldGetAuthenticatedUser() throws Exception {
        String basicDigestHeaderValue = "Basic " + new String(Base64.encode(("test-user-db@tester.com.au:password").getBytes()));

        mockMvc.perform(
                get("/user")
                        .with(testSecurityContext())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", basicDigestHeaderValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email").value("test-user-db@tester.com.au"));
    }

    @Test
    public void shouldNotGetAuthenticatedUserWithNoUserDetails() throws Exception {
        mockMvc.perform(
                get("/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
