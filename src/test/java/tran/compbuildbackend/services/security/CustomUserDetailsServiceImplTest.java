package tran.compbuildbackend.services.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomUserDetailsServiceImplTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private CustomUserDetailsService customUserDetailsService;

    @Before
    public void setUp() {
        customUserDetailsService = new CustomUserDetailsServiceImpl(applicationUserRepository);
    }

    /*
     * This test checks if we can successfully grab a user that exists.
     */
    @Test
    public void loadUserByUsernameSuccess() {
        UserDetails user = customUserDetailsService.loadUserByUsername(USER_NAME_ONE);

        checkFirstUser(user);
    }

    /*
     * This test checks if we can grab a user that does not exist, and if not then an exception is expected to be thrown.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameFailure() {
        customUserDetailsService.loadUserByUsername(USER_NAME_FAIL_LOOK_UP);
    }

    /*
     * This test will check if we can load the first user by the id.
     */
    @Test
    public void loadApplicationUserByIdSuccess() {
        ApplicationUser user = customUserDetailsService.loadApplicationUserById(FIRST_ID);

        checkFirstUser(user);
        assertEquals(FIRST_ID, user.getId());
    }

    /*
     * This test will check if we can load a user with an invalid id (an id with a negative value) which will not match
     * any user in the database.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void loadApplicationUserByIdFailure() {
        customUserDetailsService.loadApplicationUserById(INVALID_ID);
    }

    /**
     * helper method to check if certain fields are valid.
     * @param user The user object to be checked.
     */
    private void checkFirstUser(UserDetails user) {
        assertNotNull(user);
        assertEquals(USER_NAME_ONE, user.getUsername());
        assertNotNull(user.getPassword());
        assertTrue(user.isEnabled());
    }
}
