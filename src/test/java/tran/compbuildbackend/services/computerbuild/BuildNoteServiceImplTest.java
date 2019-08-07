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
import tran.compbuildbackend.domain.computerbuild.BuildNote;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.utility.BuildNoteUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.repositories.computerbuild.BuildNoteRepository;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
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
public class BuildNoteServiceImplTest {

    private BuildNoteService buildNoteService;

    @Autowired
    private BuildNoteRepository buildNoteRepository;

    @Autowired
    private ComputerBuildRepository computerBuildRepository;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ComputerBuildService computerBuildService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Before
    public void setUp() {
        buildNoteService = new BuildNoteServiceImpl(buildNoteRepository, computerBuildRepository);
        computerBuildService = new ComputerBuildServiceImpl(computerBuildRepository, applicationUserRepository);
    }

    @Transactional
    @Test
    public void createBuildNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void createBuildNoteNotLoggedIn() {
        createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void createBuildNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createBuildNote(DEFAULT_PRIORITY, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void updateBuildNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE, TEST_BUILD_NOTE_LIST_NOTE_TWO);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void updateBuildNoteNotLoggedIn() {
        updateBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE, TEST_BUILD_NOTE_LIST_NOTE_TWO);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void updateBuildNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);

        updateBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE, null);

        logUserOut();
    }

    @Transactional
    @Test
    public void deleteBuildNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        BuildNote buildNote = createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        String buildNoteIdentifier = buildNote.getUniqueIdentifier();

        BuildNote retrievedBuildNote = buildNoteRepository.getBuildNoteByUniqueIdentifier(buildNoteIdentifier);
        assertNotNull(retrievedBuildNote);
        buildNoteService.delete(buildNoteIdentifier);

        BuildNote nullNote = buildNoteRepository.getBuildNoteByUniqueIdentifier(buildNoteIdentifier);
        assertNull(nullNote);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void deleteBuildNoteNotLoggedIn() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        BuildNote buildNote = createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String buildNoteIdentifier = buildNote.getUniqueIdentifier();
        BuildNote retrievedBuildNote = buildNoteRepository.getBuildNoteByUniqueIdentifier(buildNoteIdentifier);
        assertNotNull(retrievedBuildNote);

        buildNoteService.delete(buildNoteIdentifier);
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void deleteBuildNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        BuildNote buildNote = createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        String buildNoteIdentifier = buildNote.getUniqueIdentifier();

        BuildNote retrievedBuildNote = buildNoteRepository.getBuildNoteByUniqueIdentifier(buildNoteIdentifier);
        assertNotNull(retrievedBuildNote);

        buildNoteService.delete(buildNoteIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }

    @Transactional
    @Test
    public void getBuildNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        BuildNote buildNote = createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String buildNoteIdentifier = buildNote.getUniqueIdentifier();
        BuildNote retrievedBuildNote = buildNoteService.getFromUniqueIdentifier(buildNoteIdentifier);
        assertNotNull(retrievedBuildNote);

        assertEquals(buildNote.getDescription(), retrievedBuildNote.getDescription());
        assertEquals(buildNote.getUniqueIdentifier(), retrievedBuildNote.getUniqueIdentifier());
        assertEquals(buildNote.getComputerBuild().getBuildIdentifier(), retrievedBuildNote.getComputerBuild().getBuildIdentifier());

    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void getBuildNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        BuildNote buildNote = createBuildNote(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String buildNoteIdentifier = buildNote.getUniqueIdentifier();
        buildNoteService.getFromUniqueIdentifier(buildNoteIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }

    private BuildNote createBuildNote(int priority, String description) {

        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS);
        ComputerBuild retrievedBuild = computerBuilds.iterator().next();
        assertNotNull(retrievedBuild);

        BuildNote buildNote = BuildNoteUtility.createBuildNote(priority, description);
        BuildNote createdBuildNote = buildNoteService.create(retrievedBuild.getBuildIdentifier(), buildNote);

        assertNotNull(createdBuildNote);
        assertNotNull(createdBuildNote.getUniqueIdentifier());
        assertNotNull(createdBuildNote.getComputerBuild());
        assertNotNull(createdBuildNote.getDescription());
        assertEquals(buildNote.getPriority(), createdBuildNote.getPriority());
        return createdBuildNote;
    }

    private void updateBuildNote(int priority, String noteDescription, String newDescription) {
        BuildNote updatedBuildNote = createBuildNote(priority, noteDescription);

        String oldDescription = updatedBuildNote.getDescription();

        updatedBuildNote.setDescription(newDescription);

        BuildNote newBuildNote = buildNoteService.update(updatedBuildNote, updatedBuildNote.getUniqueIdentifier());
        assertNotEquals(oldDescription, newBuildNote.getDescription());
        assertEquals(newDescription, newBuildNote.getDescription());
    }
}
