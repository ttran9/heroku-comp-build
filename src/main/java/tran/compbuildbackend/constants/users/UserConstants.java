package tran.compbuildbackend.constants.users;

public class UserConstants {
    public static final String USER_NAME_ONE = "toddtran10";
    public static final String USER_ONE_EMAIL = "toddtran10@gmail.com";
    public static final String FULL_NAME_ONE = "Dwight Schrute";
    public static final String USER_PASSWORD = "password";

    public static final String USER_NAME_TWO = "wptran58";
    public static final String USER_TWO_EMAIL = "wptran58@gmail.com";
    public static final String FULL_NAME_TWO = "David Wallace";

    public static final String USER_NAME_THREE = "mdttran2000";
    public static final String USER_THREE_EMAIL = "mdttran2000@gmail.com";
    public static final String FULL_NAME_THREE = "Andy Bernard";

    // constants used for tests.
    public static final Long FIRST_ID = 1L;
    public static final Long INVALID_ID = -1L;

    public static final String SUCCESSFUL_USER_NAME = "toddtran9";
    public static final String SUCCESSFUL_USER_EMAIL = "toddtran9@gmail.com";
    public static final String FULL_NAME = "Jim Halpert";
    public static final String USER_PASSWORD_TWO = "password1";
    public static final String SHORT_PASSWORD = "pasrd";

    public static final String SUCCESSFUL_USER_NAME_TWO = "toddblog1";
    public static final String SUCCESSFUL_USER_EMAIL_TWO = "toddblog1@gmail.com";
    public static final String MODIFIED_PASSWORD = "passwordnew";

    public static final String USER_NAME_DOES_NOT_EXIST = "idontexistfortesting";
    public static final String USER_EMAIL_DOES_NOT_EXIST = "idontexistfortesting@gmail.com";
    public static final String USER_NAME_DOES_NOT_EXIST_TWO = "idontexistfortestingtwo";
    public static final String USER_EMAIL_DOES_NOT_EXIST_TWO = "idontexistfortestingtwo@gmail.com";
    public static final String USER_NAME_FAIL_LOOK_UP = "anonexistantemail";
    public static final String USER_EMAIL_FAIL_LOOK_UP = "anonexistantemail@gmail.com";

    public static final String USER_NAME_TO_CREATE_NEW_USER = "toddtran20";
    public static final String USER_EMAIL_TO_CREATE_NEW_USER = "toddtran20@gmail.com";

    // the below is used to do the login for the tests to get the jwt token to test the controllers (except the user controller).
    public static final String ANOTHER_USER_NAME_TO_CREATE_NEW_USER = "toddtran21";
    public static final String ANOTHER_EMAIL_TO_CREATE_NEW_USER = "toddtran21@gmail.com";
    public static final String USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS = "vacuumluigi";
    public static final String EMAIL_TO_TEST_OWNERSHIP_ENDPOINTS = "vacuumluigi@yahoo.com";
}
