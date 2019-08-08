package tran.compbuildbackend.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import tran.compbuildbackend.domain.computerbuild.*;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.domain.utility.DateUtility;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.computerbuild.*;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;
import tran.compbuildbackend.services.users.ApplicationUserService;

import java.util.LinkedList;
import java.util.List;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserIn;
import static tran.compbuildbackend.controllers.utility.WebUtility.logUserOut;

@Component
@Profile({"test"})
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    private ApplicationUserService applicationUserService;
    private ComputerBuildService computerBuildService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ApplicationUserAuthenticationService authenticationService;

    @Autowired
    private ComputerPartService computerPartService;

    @Autowired
    private DirectionService directionService;

    @Autowired
    private BuildNoteService buildNoteService;

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private OverclockingNoteService overclockingNoteService;

    @Autowired
    public BootstrapData(ApplicationUserService applicationUserService, ComputerBuildService computerBuildService) {
        this.applicationUserService = applicationUserService;
        this.computerBuildService = computerBuildService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createUsers();
        createSampleBuilds();
    }

    private void createSampleBuilds() {
        LoginRequest loginRequest = new LoginRequest(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createSampleComputerBuilds();
        logUserOut();

        createComputerBuildsWithNoDetails();
    }

    private void createUsers() {
        // create a user that is enabled
        ApplicationUser user = createUser(USER_NAME_ONE, USER_ONE_EMAIL, USER_PASSWORD, FULL_NAME_ONE);

        // create a user with an account that is not enabled.
        ApplicationUser secondUser = createUser(USER_NAME_TWO, USER_TWO_EMAIL, USER_PASSWORD, FULL_NAME_TWO);

        // create a user with an account that is not enabled.
        ApplicationUser thirdUser = createUser(USER_NAME_THREE, USER_THREE_EMAIL, USER_PASSWORD, FULL_NAME_THREE);

        // create another account that is used only to log in to get a JWT, it will not be used for other tests.
        ApplicationUser userForLogin = createUser(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, ANOTHER_EMAIL_TO_CREATE_NEW_USER,
                USER_PASSWORD, FULL_NAME_THREE);

        // a user account used to login as a second user when trying operations that require an original owner.
        ApplicationUser userForSecondLogin = createUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, EMAIL_TO_TEST_OWNERSHIP_ENDPOINTS,
                USER_PASSWORD, FULL_NAME);

        // a user account used to login as a second user when trying operations that require an original owner.
        ApplicationUser userForThirdLogin = createUser(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS, EMAIL_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS,
                USER_PASSWORD, FULL_NAME_CONTROLLERS);

        // save/create users
        applicationUserService.persistUser(user, null);
        applicationUserService.enableUser(user);

        applicationUserService.persistUser(secondUser, null);

        applicationUserService.persistUser(thirdUser, null);
        applicationUserService.persistUser(userForLogin, null);
        applicationUserService.enableUser(userForLogin);

        applicationUserService.persistUser(userForSecondLogin, null);
        applicationUserService.enableUser(userForSecondLogin);

        applicationUserService.persistUser(userForThirdLogin, null);
        applicationUserService.enableUser(userForThirdLogin);


    }

    private ApplicationUser createUser(String userName, String email, String password, String fullName) {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        return user;
    }

    private void createSampleComputerBuilds() {
        List<Direction> budgetDirections = new LinkedList<>();
        addDirections(budgetDirections, BUDGET_FIRST_DIRECTION_DESCRIPTION);
        addDirections(budgetDirections, BUDGET_SECOND_DIRECTION_DESCRIPTION);
        addDirections(budgetDirections, BUDGET_THIRD_DIRECTION_DESCRIPTION);

        List<OverclockingNote> budgetOverclockingNotes = new LinkedList<>();
        addOverclockingNotes(budgetOverclockingNotes, BUDGET_OVERCLOCKING_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addOverclockingNotes(budgetOverclockingNotes, BUDGET_OVERCLOCKING_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addOverclockingNotes(budgetOverclockingNotes, BUDGET_OVERCLOCKING_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<Purpose> budgetPurposeList = new LinkedList<>();
        addPurpose(budgetPurposeList, BUDGET_PURPOSE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addPurpose(budgetPurposeList, BUDGET_PURPOSE_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addPurpose(budgetPurposeList, BUDGET_PURPOSE_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<BuildNote> budgetBuildNotes = new LinkedList<>();
        addBuildNotes(budgetBuildNotes, BUDGET_BUILD_NOTE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addBuildNotes(budgetBuildNotes, BUDGET_BUILD_NOTE_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addBuildNotes(budgetBuildNotes, BUDGET_BUILD_NOTE_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<ComputerPart> budgetComputerParts = new LinkedList<>();
        addComputerParts(budgetComputerParts, BUDGET_COMPUTER_PART_NAME_ONE, BUDGET_COMPUTER_PART_PRICE_ONE,
                BUDGET_COMPUTER_PART_PURCHASE_LOCATION_ONE, BUDGET_COMPUTER_PART_OTHER_NOTES_ONE, BUDGET_COMPUTER_PART_PURCHASE_DATE_ONE);
        addComputerParts(budgetComputerParts, BUDGET_COMPUTER_PART_NAME_TWO, BUDGET_COMPUTER_PART_PRICE_TWO,
                BUDGET_COMPUTER_PART_PURCHASE_LOCATION_TWO, BUDGET_COMPUTER_PART_OTHER_NOTES_TWO, BUDGET_COMPUTER_PART_PURCHASE_DATE_TWO);
        addComputerParts(budgetComputerParts, BUDGET_COMPUTER_PART_NAME_THREE, BUDGET_COMPUTER_PART_PRICE_THREE,
                BUDGET_COMPUTER_PART_PURCHASE_LOCATION_THREE, BUDGET_COMPUTER_PART_OTHER_NOTES_THREE, BUDGET_COMPUTER_PART_PURCHASE_DATE_THREE);
        createSampleComputerBuild(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_BUDGET_COMPUTER_BUILD_DESCRIPTION,
                budgetDirections, budgetComputerParts, budgetOverclockingNotes, budgetPurposeList, budgetBuildNotes);

        List<Direction> gamingDirections = new LinkedList<>();
        addDirections(gamingDirections, GAMING_FIRST_DIRECTION_DESCRIPTION);
        addDirections(gamingDirections, GAMING_SECOND_DIRECTION_DESCRIPTION);
        addDirections(gamingDirections, GAMING_THIRD_DIRECTION_DESCRIPTION);
        addDirections(gamingDirections, GAMING_FOURTH_DIRECTION_DESCRIPTION);

        List<OverclockingNote> gamingOverclockingNotes = new LinkedList<>();
        addOverclockingNotes(gamingOverclockingNotes, GAMING_OVERCLOCKING_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addOverclockingNotes(gamingOverclockingNotes, GAMING_OVERCLOCKING_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addOverclockingNotes(gamingOverclockingNotes, GAMING_OVERCLOCKING_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<Purpose> gamingPurposeList = new LinkedList<>();
        addPurpose(gamingPurposeList, GAMING_PURPOSE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addPurpose(gamingPurposeList, GAMING_PURPOSE_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addPurpose(gamingPurposeList, GAMING_PURPOSE_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<BuildNote> gamingBuildNotes = new LinkedList<>();
        addBuildNotes(gamingBuildNotes, GAMING_BUILD_NOTE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
        addBuildNotes(gamingBuildNotes, GAMING_BUILD_NOTE_LIST_NOTE_TWO, DEFAULT_PRIORITY);
        addBuildNotes(gamingBuildNotes, GAMING_BUILD_NOTE_LIST_NOTE_THREE, DEFAULT_PRIORITY);

        List<ComputerPart> gamingComputerParts = new LinkedList<>();
        addComputerParts(gamingComputerParts, GAMING_COMPUTER_PART_NAME_ONE, GAMING_COMPUTER_PART_PRICE_ONE,
                GAMING_COMPUTER_PART_PURCHASE_LOCATION_ONE, GAMING_COMPUTER_PART_OTHER_NOTES_ONE, GAMING_COMPUTER_PART_PURCHASE_DATE_ONE);
        addComputerParts(gamingComputerParts, GAMING_COMPUTER_PART_NAME_TWO, GAMING_COMPUTER_PART_PRICE_TWO,
                GAMING_COMPUTER_PART_PURCHASE_LOCATION_TWO, GAMING_COMPUTER_PART_OTHER_NOTES_TWO, GAMING_COMPUTER_PART_PURCHASE_DATE_TWO);
        addComputerParts(gamingComputerParts, GAMING_COMPUTER_PART_NAME_THREE, GAMING_COMPUTER_PART_PRICE_THREE,
                GAMING_COMPUTER_PART_PURCHASE_LOCATION_THREE, GAMING_COMPUTER_PART_OTHER_NOTES_THREE, GAMING_COMPUTER_PART_PURCHASE_DATE_THREE);
        createSampleComputerBuild(SAMPLE_GAMING_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION,
                gamingDirections, gamingComputerParts, gamingOverclockingNotes, gamingPurposeList, gamingBuildNotes);
    }

    private void createComputerBuildsWithNoDetails() {
        // create a computer build with a name a description but empty lists of details.
        LoginRequest loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createSampleBuildWithEmptySubLists(FIRST_TEST_BUILD_NAME, FIRST_TEST_BUILD_DESCRIPTION);
        logUserOut();

        loginRequest = new LoginRequest(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS, USER_PASSWORD);
        logUserIn(authenticationService, authenticationManager, jwtTokenProvider, loginRequest);
        createSampleBuildWithEmptySubLists(SECOND_TEST_BUILD_NAME, SECOND_TEST_BUILD_DESCRIPTION);
        logUserOut();

    }

    private void createSampleBuildWithEmptySubLists(String buildName, String buildDescription) {
        createSampleComputerBuild(buildName, buildDescription, new LinkedList<>(), new LinkedList<>(),
                new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
    }

    private void addOverclockingNotes(List<OverclockingNote> overclockingNotes, String description, int priority) {
        OverclockingNote overclockingNote = new OverclockingNote();
        overclockingNote.setDescription(description);
        overclockingNote.setPriority(priority);
        overclockingNotes.add(overclockingNote);
    }

    private void createOverclockingNote(ComputerBuild computerBuild, OverclockingNote overclockingNote) {
        overclockingNoteService.create(computerBuild.getBuildIdentifier(), overclockingNote);
    }

    private void addPurpose(List<Purpose> purposeList, String description, int priority) {
        Purpose purpose = new Purpose();
        purpose.setDescription(description);
        purpose.setPriority(priority);
        purposeList.add(purpose);
    }

    private void createPurpose(ComputerBuild computerBuild, Purpose purpose) {
        purposeService.create(computerBuild.getBuildIdentifier(), purpose);
    }

    private void addBuildNotes(List<BuildNote> buildNotes, String description, int priority) {
        BuildNote buildNote = new BuildNote();
        buildNote.setDescription(description);
        buildNote.setPriority(priority);
        buildNotes.add(buildNote);
    }

    private void createBuildNote(ComputerBuild computerBuild, BuildNote buildNote) {
        buildNoteService.create(computerBuild.getBuildIdentifier(), buildNote);
    }



    private void addComputerParts(List<ComputerPart> computerParts, String partName, double partPrice,
                                                String partPurchaseLocation, String partOtherNotes, String purchaseDate) {
        ComputerPart gamingComputerPart = new ComputerPart();
        gamingComputerPart.setName(partName);
        gamingComputerPart.setPrice(partPrice);
        gamingComputerPart.setPlacePurchasedAt(partPurchaseLocation);
        gamingComputerPart.setOtherNote(partOtherNotes);
        gamingComputerPart.setPurchaseDate(DateUtility.convertStringToDate(purchaseDate));
        computerParts.add(gamingComputerPart);
    }


    private void createSampleComputerBuild(String computerBuildName, String computerBuildDescription, List<Direction> directions,
                                           List<ComputerPart> computerParts, List<OverclockingNote> overclockingNotes,
                                           List<Purpose> purposeList, List<BuildNote> buildNotes) {
        ComputerBuild computerBuild = new ComputerBuild();
        computerBuild.setName(computerBuildName);
        computerBuild.setBuildDescription(computerBuildDescription);

        computerBuildService.createNewComputerBuild(computerBuild);

        for(Direction direction : directions) {
            createDirections(computerBuild, direction);

        }

        for(ComputerPart computerPart : computerParts) {
            createComputerParts(computerBuild, computerPart);
        }

        for(OverclockingNote overclockingNote : overclockingNotes) {
            createOverclockingNote(computerBuild, overclockingNote);
        }

        for(Purpose purpose : purposeList) {
            createPurpose(computerBuild, purpose);
        }

        for(BuildNote buildNote : buildNotes) {
            createBuildNote(computerBuild, buildNote);
        }


    }

    private void createDirections(ComputerBuild computerBuild, Direction direction) {
        directionService.create(computerBuild.getBuildIdentifier(), direction);
    }

    private void addDirections(List<Direction> directions, String description) {
        Direction direction = new Direction();
        direction.setDescription(description);
        directions.add(direction);
    }

    private void createComputerParts(ComputerBuild computerBuild, ComputerPart computerPart) {
        computerPartService.create(computerBuild.getBuildIdentifier(), computerPart);
    }
}
