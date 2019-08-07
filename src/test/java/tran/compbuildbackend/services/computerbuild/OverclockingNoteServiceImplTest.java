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
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;
import tran.compbuildbackend.domain.utility.OverclockingNoteUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.OverclockingNoteRepository;
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
public class OverclockingNoteServiceImplTest {

    private OverclockingNoteService overclockingNoteService;

    private ComputerBuildService computerBuildService;

    @Autowired
    private ComputerBuildRepository computerBuildRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private OverclockingNoteRepository overclockingNoteRepository;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before
    public void setUp() {
        computerBuildService = new ComputerBuildServiceImpl(computerBuildRepository, applicationUserRepository);
        overclockingNoteService = new OverclockingNoteServiceImpl(overclockingNoteRepository, computerBuildRepository);
    }

    @Transactional
    @Test
    public void createOverclockingNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void createOverclockingNoteNotLoggedIn() {
        createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void createOverclockingNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createOverclockingNote(DEFAULT_PRIORITY, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void updateOverclockingNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, TEST_OVERCLOCKING_NOTE_LIST_NOTE_TWO);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updateOverclockingNoteNotLoggedIn() {
        updateOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, TEST_OVERCLOCKING_NOTE_LIST_NOTE_TWO);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void updateOverclockingNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, null);
        logUserOut();
    }

    @Transactional
    @Test
    public void deleteOverclockingNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        OverclockingNote overclockingNote = createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        String overclockingNoteUniqueIdentifier = overclockingNote.getUniqueIdentifier();

        OverclockingNote retrievedOverclockingNote = overclockingNoteRepository.getOverclockingNoteByUniqueIdentifier(overclockingNoteUniqueIdentifier);
        assertNotNull(retrievedOverclockingNote);
        overclockingNoteService.delete(retrievedOverclockingNote.getUniqueIdentifier());

        OverclockingNote nullOverclockingNote = overclockingNoteRepository.getOverclockingNoteByUniqueIdentifier(retrievedOverclockingNote.getUniqueIdentifier());
        assertNull(nullOverclockingNote);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void deleteOverclockingNoteNotLoggedIn() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        OverclockingNote overclockingNote = createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String overclockingNoteUniqueIdentifier = overclockingNote.getUniqueIdentifier();
        OverclockingNote retrievedOverclockingNote = overclockingNoteRepository.getOverclockingNoteByUniqueIdentifier(overclockingNoteUniqueIdentifier);
        assertNotNull(retrievedOverclockingNote);

        overclockingNoteService.delete(retrievedOverclockingNote.getUniqueIdentifier());
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void deleteOverclockingNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        OverclockingNote overclockingNote = createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        String overclockingNoteUniqueIdentifier = overclockingNote.getUniqueIdentifier();

        OverclockingNote retrievedOverclockingNote = overclockingNoteRepository.getOverclockingNoteByUniqueIdentifier(overclockingNoteUniqueIdentifier);
        assertNotNull(retrievedOverclockingNote);
        overclockingNoteService.delete(retrievedOverclockingNote.getUniqueIdentifier() + INVALID_IDENTIFIER_SUFFIX);
    }

    @Transactional
    @Test
    public void getOverclockingNoteSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        OverclockingNote createdOverclockingNote = createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String overclockingNoteUniqueIdentifier = createdOverclockingNote.getUniqueIdentifier();
        OverclockingNote retrievedOverclockingNote = overclockingNoteService.getFromUniqueIdentifier(overclockingNoteUniqueIdentifier);
        assertNotNull(retrievedOverclockingNote);

        assertEquals(createdOverclockingNote.getDescription(), retrievedOverclockingNote.getDescription());
        assertEquals(createdOverclockingNote.getUniqueIdentifier(), retrievedOverclockingNote.getUniqueIdentifier());
        assertEquals(createdOverclockingNote.getComputerBuild().getBuildIdentifier(), retrievedOverclockingNote.getComputerBuild().getBuildIdentifier());
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void getOverclockingNoteFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        OverclockingNote createdOverclockingNote = createOverclockingNote(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        logUserOut();

        String overclockingNoteUniqueIdentifier = createdOverclockingNote.getUniqueIdentifier();
        overclockingNoteService.getFromUniqueIdentifier(overclockingNoteUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }

    private OverclockingNote createOverclockingNote(int priority, String description) {

        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS);
        ComputerBuild retrievedBuild = computerBuilds.iterator().next();
        assertNotNull(retrievedBuild);

        OverclockingNote overclockingNote = OverclockingNoteUtility.createOverclockingNote(priority, description);
        OverclockingNote createdOverclockingNote = overclockingNoteService.create(retrievedBuild.getBuildIdentifier(), overclockingNote);

        assertNotNull(createdOverclockingNote);
        assertNotNull(createdOverclockingNote.getUniqueIdentifier());
        assertNotNull(createdOverclockingNote.getComputerBuild());
        assertNotNull(createdOverclockingNote.getDescription());
        assertEquals(createdOverclockingNote.getPriority(), createdOverclockingNote.getPriority());
        return createdOverclockingNote;
    }

    private void updateOverclockingNote(int priority, String noteDescription, String newDescription) {
        OverclockingNote overclockingNote = createOverclockingNote(priority, noteDescription);

        String oldDescription = overclockingNote.getDescription();

        overclockingNote.setDescription(newDescription);

        OverclockingNote updatedOverclockingNote = overclockingNoteService.update(overclockingNote, overclockingNote.getUniqueIdentifier());
        assertNotEquals(oldDescription, updatedOverclockingNote.getDescription());
        assertEquals(newDescription, updatedOverclockingNote.getDescription());
    }
}
