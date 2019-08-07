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
import tran.compbuildbackend.domain.computerbuild.ComputerPart;
import tran.compbuildbackend.domain.utility.ComputerPartUtility;
import tran.compbuildbackend.domain.utility.DateUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.ComputerPartRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS;
import static tran.compbuildbackend.constants.users.UserConstants.USER_PASSWORD;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserIn;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserOut;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile("test")
public class ComputerPartServiceImplTest {

    private ComputerPartService computerPartService;

    private ComputerBuildService computerBuildService;

    @Autowired
    private ComputerBuildRepository computerBuildRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ComputerPartRepository computerPartRepository;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before
    public void setUp() {
        computerBuildService = new ComputerBuildServiceImpl(computerBuildRepository, applicationUserRepository);
        computerPartService = new ComputerPartServiceImpl(computerPartRepository, computerBuildRepository);
    }

    @Transactional
    @Test
    public void createComputerPartSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void createComputerPartNotLoggedIn() {
        createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void createComputerPartFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createComputerPart(null, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();
    }

    @Test
    @Transactional
    public void updateComputerPartSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void updateComputerPartNotLoggedIn() {
        updateComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
    }

    @Transactional
    @Test(expected = ConstraintViolationException.class)
    public void updateComputerPartFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        updateComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, null,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();
    }

    @Transactional
    @Test
    public void deleteComputerPartSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        ComputerPart computerPart = createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        String computerPartIdentifier = computerPart.getUniqueIdentifier();

        ComputerPart retrievedComputerPart = computerPartRepository.getComputerPartByUniqueIdentifier(computerPartIdentifier);
        assertNotNull(retrievedComputerPart);
        computerPartService.delete(retrievedComputerPart.getUniqueIdentifier());

        ComputerPart nullComputerPart = computerPartRepository.getComputerPartByUniqueIdentifier(retrievedComputerPart.getUniqueIdentifier());
        assertNull(nullComputerPart);
        logUserOut();
    }

    @Transactional
    @Test(expected = UsernameNotFoundException.class)
    public void deleteComputerPartNotLoggedIn() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        ComputerPart computerPart = createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();

        String computerPartIdentifier = computerPart.getUniqueIdentifier();
        ComputerPart retrievedComputerPart = computerPartRepository.getComputerPartByUniqueIdentifier(computerPartIdentifier);
        assertNotNull(retrievedComputerPart);

        computerPartService.delete(retrievedComputerPart.getUniqueIdentifier());
    }

    @Transactional
    @Test(expected = GenericRequestException.class)
    public void deleteComputerPartFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        ComputerPart computerPart = createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        String computerPartIdentifier = computerPart.getUniqueIdentifier();

        ComputerPart retrievedComputerPart = computerPartRepository.getComputerPartByUniqueIdentifier(computerPartIdentifier);
        assertNotNull(retrievedComputerPart);

        computerPartService.delete(retrievedComputerPart.getUniqueIdentifier() + INVALID_IDENTIFIER_SUFFIX);
    }

    @Transactional
    @Test
    public void getComputerPartSuccess() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        ComputerPart computerPart = createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();

        String computerPartIdentifier = computerPart.getUniqueIdentifier();

        ComputerPart retrievedComputerPart = computerPartService.getFromUniqueIdentifier(computerPartIdentifier);
        assertNotNull(retrievedComputerPart);

        assertEquals(computerPart.getName(), retrievedComputerPart.getName());
        assertEquals(computerPart.getPurchaseDate(), retrievedComputerPart.getPurchaseDate());
        assertEquals(computerPart.getPlacePurchasedAt(), retrievedComputerPart.getPlacePurchasedAt());
        assertEquals(computerPart.getPrice(), retrievedComputerPart.getPrice(), 0); // the prices should match exactly so delta of 0.
        assertEquals(computerPart.getOtherNote(), retrievedComputerPart.getOtherNote());
        assertEquals(computerPart.getUniqueIdentifier(), retrievedComputerPart.getUniqueIdentifier());
        assertNotNull(computerPart.getComputerBuild());
    }


    @Transactional
    @Test(expected = GenericRequestException.class)
    public void getComputerPartFailure() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        ComputerPart computerPart = createComputerPart(TEST_COMPUTER_PART_NAME, DateUtility.convertStringToDate(TEST_COMPUTER_PART_PURCHASE_DATE),
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_OTHER_NOTES);
        logUserOut();

        String computerPartIdentifier = computerPart.getUniqueIdentifier();

        computerPartService.getFromUniqueIdentifier(computerPartIdentifier + INVALID_IDENTIFIER_SUFFIX);
    }


    private ComputerPart createComputerPart(String name, LocalDate localDate, String placePurchasedAt, double price,
                                            String otherNotes) {
        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS);
        ComputerBuild retrievedBuild = computerBuilds.iterator().next();
        assertNotNull(retrievedBuild);


        ComputerPart computerPart = ComputerPartUtility.createComputerPart(name, localDate, placePurchasedAt, price, otherNotes);
        ComputerPart createdComputerPart = computerPartService.create(retrievedBuild.getBuildIdentifier(), computerPart);

        assertNotNull(createdComputerPart);
        assertNotNull(createdComputerPart.getUniqueIdentifier());
        assertNotNull(createdComputerPart.getComputerBuild());
        assertNotNull(createdComputerPart.getName());
        assertNotNull(createdComputerPart.getPlacePurchasedAt());
        assertNotNull(createdComputerPart.getOtherNote());
        assertNotNull(createdComputerPart.getId());
        return createdComputerPart;
    }

    private void updateComputerPart(String name, LocalDate localDate, String placePurchasedAt, String newPlacePurchasedAt,
                                    double price, String oldNotes) {
        ComputerPart computerPart = createComputerPart(name, localDate, placePurchasedAt, price, oldNotes);

        String oldPlacePurchasedAt = computerPart.getPlacePurchasedAt();

        computerPart.setPlacePurchasedAt(newPlacePurchasedAt);

        ComputerPart newComputerPart = computerPartService.update(computerPart, computerPart.getUniqueIdentifier());
        assertNotEquals(oldPlacePurchasedAt, newComputerPart.getPlacePurchasedAt());
        assertEquals(newPlacePurchasedAt, newComputerPart.getPlacePurchasedAt());
    }

}
