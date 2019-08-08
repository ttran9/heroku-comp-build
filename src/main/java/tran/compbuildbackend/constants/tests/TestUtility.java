package tran.compbuildbackend.constants.tests;

public class TestUtility {
    public static final String BASE_URL = "http://localhost:8080";

    // key constants
    public static final String BUILD_IDENTIFIER_KEY = "buildIdentifier";
    public static final String PURCHASE_DATE_KEY = "purchaseDate";
    public static final String NAME_KEY = "name";
    public static final String PRICE_KEY = "price";
    public static final String UNIQUE_IDENTIFIER_KEY = "uniqueIdentifier";
    public static final String PLACE_PURCHASED_AT_KEY = "placePurchasedAt";
    public static final String OTHER_NOTES_KEY = "otherNote";
    public static final String COMPUTER_BUILD_ID_KEY = "computerBuildId";
    public static final String DESCRIPTION_KEY = "description";
    public static final String PRIORITY_KEY = "priority";
    public static final String TOTAL_PRICE_KEY = "totalPrice";

    // computer build test constants.
    public static final String SAMPLE_BUDGET_COMPUTER_BUILD_NAME = "Budget Comp Build";
    public static final String SAMPLE_GAMING_COMPUTER_BUILD_NAME = "Gaming Comp Build";
    public static final String FIRST_TEST_BUILD_NAME = "Test Build!!";
    public static final String SECOND_TEST_BUILD_NAME = "Number Two Test Build!!";

    public static final String SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION = "This is my gaming comp build for 2019";
    public static final String SAMPLE_BUDGET_COMPUTER_BUILD_DESCRIPTION = "This is my budget comp build for 2019!!";
    public static final String FIRST_TEST_BUILD_DESCRIPTION = "This is a build that I am creating for the automated tests (for services).";
    public static final String SECOND_TEST_BUILD_DESCRIPTION = "This is the second build that I am creating for the automated tests (for controllers).";

    public static final String FIELD_CANNOT_BE_NULL = "must not be null";
    public static final String FIELD_CANNOT_BE_EMPTY = "must not be empty";
    public static final String FIELD_CANNOT_BE_BLANK = "must not be blank";

    // constants used in multiple tests.
    public static final String INVALID_IDENTIFIER_SUFFIX = "1";
    public static final String EMPTY_CONTENT = "";

    // build note constants for testing.
    public static final String TEST_BUILD_NOTE_LIST_NOTE_ONE = "This is the first build note test.";
    public static final String TEST_BUILD_NOTE_LIST_NOTE_TWO = "This is the first build note test new.";

    // computer part constants for testing
    public static final String TEST_COMPUTER_PART_NAME = "Ryzen 7 3700x";
    public static final String TEST_COMPUTER_PART_PURCHASE_DATE = "2019-08-16";
    public static final String TEST_COMPUTER_PART_PLACE_PURCHASED_AT = "Mountain View Bestbuy";
    public static final String TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO = "Livermore Bestbuy";
    public static final String TEST_COMPUTER_PART_OTHER_NOTES = "This is a really good processor for high performance (not the top tier) gaming. This is also very good for hobbyists using productivity or light video editing/content production.";
    public static final double TEST_COMPUTER_PART_PRICE = 330.00;
    public static final double TEST_COMPUTER_PART_PRICE_TWO = 320.00;
    public static final double TEST_COMPUTER_PART_PRICE_TOO_HIGH = 10000000.00;
    public static final double TEST_COMPUTER_PART_PRICE_IMPROPER_FORMAT = 500.0001;
    public static final double EXPECTED_DIFFERENCE = 0;

    // direction constants for testing.
    public static final String TEST_DIRECTION_DESCRIPTION = "This is the first direction step.";
    public static final String TEST_DIRECTION_DESCRIPTION_UPDATED = "This is the first direction step updated!.";

    // overclocking note constants for testing.
    public static final String TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE = "This is the first overclocking note test.";
    public static final String TEST_OVERCLOCKING_NOTE_LIST_NOTE_TWO = "This is the first overclocking note test new.";

    // purpose constants for testing.
    public static final String TEST_PURPOSE_LIST_NOTE_ONE = "This is the first purpose note test.";
    public static final String TEST_PURPOSE_LIST_NOTE_TWO = "This is the first purpose note test new.";
}
