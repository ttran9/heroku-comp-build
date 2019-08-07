package tran.compbuildbackend.services.computerbuild;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.computerbuild.Direction;
import tran.compbuildbackend.domain.utility.DirectionUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.DirectionRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS;
import static tran.compbuildbackend.constants.users.UserConstants.USER_PASSWORD;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserIn;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserOut;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile("test")
public class DirectionServiceImplTest {

    private DirectionService directionService;

    private ComputerBuildService computerBuildService;

    @Autowired
    private ComputerBuildRepository computerBuildRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before
    public void setUp() {
        directionService = new DirectionServiceImpl(directionRepository, computerBuildRepository);
        computerBuildService = new ComputerBuildServiceImpl(computerBuildRepository, applicationUserRepository);
    }

    @Transactional
    @Test
    public void createDirectionSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createDirection(TEST_DIRECTION_DESCRIPTION);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void createDirectionNotLoggedIn() {
        createDirection(TEST_DIRECTION_DESCRIPTION);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void createDirectionFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createDirection(null);
        logUserOut();
    }

    @Transactional
    @Test
    public void updateDirectionSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateDirection(TEST_DIRECTION_DESCRIPTION, TEST_DIRECTION_DESCRIPTION_UPDATED);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updateDirectionNotLoggedIn() {
        updateDirection(TEST_DIRECTION_DESCRIPTION, TEST_DIRECTION_DESCRIPTION_UPDATED);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void updateDirectionFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateDirection(TEST_DIRECTION_DESCRIPTION, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void deleteDirectionSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Direction direction = createDirection(TEST_DIRECTION_DESCRIPTION);
        String directionUniqueIdentifier = direction.getUniqueIdentifier();

        Direction retrievedDirection = directionRepository.getDirectionByUniqueIdentifier(directionUniqueIdentifier);
        assertNotNull(retrievedDirection);
        directionService.delete(retrievedDirection.getUniqueIdentifier());

        Direction nullDirection = directionRepository.getDirectionByUniqueIdentifier(retrievedDirection.getUniqueIdentifier());
        assertNull(nullDirection);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void deleteDirectionNotLoggedIn() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Direction direction = createDirection(TEST_DIRECTION_DESCRIPTION);
        logUserOut();

        String directionUniqueIdentifier = direction.getUniqueIdentifier();
        Direction retrievedDirection = directionRepository.getDirectionByUniqueIdentifier(directionUniqueIdentifier);
        assertNotNull(retrievedDirection);

        directionService.delete(retrievedDirection.getUniqueIdentifier());
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void deleteDirectionPartFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Direction direction = createDirection(TEST_DIRECTION_DESCRIPTION);
        String directionUniqueIdentifier = direction.getUniqueIdentifier();

        Direction retrievedDirection = directionRepository.getDirectionByUniqueIdentifier(directionUniqueIdentifier);
        assertNotNull(retrievedDirection);

        directionService.delete(retrievedDirection.getUniqueIdentifier() + INVALID_IDENTIFIER_SUFFIX);
    }

    @Transactional
    @Test
    public void getDirectionSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Direction createdDirection = createDirection(TEST_DIRECTION_DESCRIPTION);
        logUserOut();

        String directionIdentifier = createdDirection.getUniqueIdentifier();
        Direction retrievedDirection = directionService.getFromUniqueIdentifier(directionIdentifier);
        assertNotNull(retrievedDirection);

        assertEquals(createdDirection.getDescription(), retrievedDirection.getDescription());
        assertEquals(createdDirection.getUniqueIdentifier(), retrievedDirection.getUniqueIdentifier());
        assertEquals(createdDirection.getComputerBuild().getBuildIdentifier(), retrievedDirection.getComputerBuild().getBuildIdentifier());
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void getDirectionFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Direction createdDirection = createDirection(TEST_DIRECTION_DESCRIPTION);
        logUserOut();

        String directionIdentifier = createdDirection.getUniqueIdentifier();
        directionService.getFromUniqueIdentifier(directionIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }


    private Direction createDirection(String description) {
        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS);
        ComputerBuild retrievedBuild = computerBuilds.iterator().next();
        assertNotNull(retrievedBuild);

        Direction direction = DirectionUtility.createDirection(description);
        Direction createdDirection = directionService.create(retrievedBuild.getBuildIdentifier(), direction);

        assertNotNull(createdDirection);
        assertNotNull(createdDirection.getUniqueIdentifier());
        assertNotNull(createdDirection.getComputerBuild());
        assertNotNull(createdDirection.getDescription());
        assertNotNull(createdDirection.getId());
        return createdDirection;
    }

    private void updateDirection(String description, String newDescription) {
        Direction direction = createDirection(description);

        String oldDescription = direction.getDescription();

        direction.setDescription(newDescription);

        Direction newDirection = directionService.update(direction, direction.getUniqueIdentifier());
        assertNotEquals(oldDescription, newDirection.getDescription());
        assertEquals(newDescription, newDirection.getDescription());
    }
}
