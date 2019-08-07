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
import tran.compbuildbackend.domain.computerbuild.Purpose;
import tran.compbuildbackend.domain.utility.PurposeUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.PurposeRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.DEFAULT_PRIORITY;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS;
import static tran.compbuildbackend.constants.users.UserConstants.USER_PASSWORD;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserIn;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserOut;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile("test")
public class PurposeServiceImplTest {

    private PurposeService purposeService;

    private ComputerBuildService computerBuildService;

    @Autowired
    private ComputerBuildRepository computerBuildRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before
    public void setUp() {
        computerBuildService = new ComputerBuildServiceImpl(computerBuildRepository, applicationUserRepository);
        purposeService = new PurposeServiceImpl(purposeRepository, computerBuildRepository);
    }

    @Transactional
    @Test
    public void createPurposeSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void createPurposeNotLoggedIn() {
        createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void createPurposeFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createPurpose(DEFAULT_PRIORITY, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void updatePurposeSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updatePurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE, TEST_PURPOSE_LIST_NOTE_TWO);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updatePurposeNotLoggedIn() {
        updatePurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE, TEST_PURPOSE_LIST_NOTE_TWO);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void updatePurposeFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updatePurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_TWO, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void deletePurposeSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Purpose purpose = createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        String purposeUniqueIdentifier = purpose.getUniqueIdentifier();

        Purpose retrievedPurpose = purposeRepository.getPurposeByUniqueIdentifier(purposeUniqueIdentifier);
        assertNotNull(retrievedPurpose);
        purposeService.delete(purposeUniqueIdentifier);

        Purpose nullPurpose = purposeRepository.getPurposeByUniqueIdentifier(purposeUniqueIdentifier);
        assertNull(nullPurpose);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void deletePurposeNotLoggedIn() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Purpose purpose = createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        logUserOut();

        String purposeUniqueIdentifier = purpose.getUniqueIdentifier();
        Purpose retrievedPurpose = purposeRepository.getPurposeByUniqueIdentifier(purposeUniqueIdentifier);
        assertNotNull(retrievedPurpose);

        purposeService.delete(purposeUniqueIdentifier);
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void deletePurposeFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Purpose purpose = createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        String purposeUniqueIdentifier = purpose.getUniqueIdentifier();

        Purpose retrievedPurpose = purposeRepository.getPurposeByUniqueIdentifier(purposeUniqueIdentifier);
        assertNotNull(retrievedPurpose);
        purposeService.delete(purposeUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }

    @Transactional
    @Test
    public void getPurposeSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Purpose purpose = createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        logUserOut();

        String purposeUniqueIdentifier = purpose.getUniqueIdentifier();
        Purpose retrievedPurpose = purposeService.getFromUniqueIdentifier(purposeUniqueIdentifier);
        assertNotNull(retrievedPurpose);

        assertEquals(purpose.getDescription(), retrievedPurpose.getDescription());
        assertEquals(purpose.getUniqueIdentifier(), retrievedPurpose.getUniqueIdentifier());
        assertEquals(purpose.getComputerBuild().getBuildIdentifier(), retrievedPurpose.getComputerBuild().getBuildIdentifier());
    }

    @Test(expected = GenericRequestException.class)
    @Transactional
    public void getPurposeFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        Purpose purpose = createPurpose(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        logUserOut();

        String purposeUniqueIdentifier = purpose.getUniqueIdentifier();
        purposeService.getFromUniqueIdentifier(purposeUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }

    private Purpose createPurpose(int priority, String description) {
        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS);
        ComputerBuild retrievedBuild = computerBuilds.iterator().next();
        assertNotNull(retrievedBuild);

        Purpose purpose = PurposeUtility.createPurpose(priority, description);
        Purpose createdPurpose = purposeService.create(retrievedBuild.getBuildIdentifier(), purpose);

        assertNotNull(createdPurpose);
        assertNotNull(createdPurpose.getUniqueIdentifier());
        assertNotNull(createdPurpose.getComputerBuild());
        assertNotNull(createdPurpose.getDescription());
        assertEquals(createdPurpose.getPriority(), createdPurpose.getPriority());
        return createdPurpose;
    }

    private void updatePurpose(int priority, String description, String newDescription) {
        Purpose purpose = createPurpose(priority, description);
        String oldDescription = purpose.getDescription();
        purpose.setDescription(newDescription);

        Purpose updatedPurpose = purposeService.update(purpose, purpose.getUniqueIdentifier());
        assertNotEquals(oldDescription, updatedPurpose.getDescription());
        assertEquals(newDescription, updatedPurpose.getDescription());
    }

}
