package tran.compbuildbackend.constants.computerbuild;

public class ComputerBuildConstants {
    public static final int IDENTIFIER_LENGTH = 8;
    public static final String DETAIL_IDENTIFIER_SEPARATOR = "-";

    public static final int LOWEST_PRIORITY = 1;
    public static final int DEFAULT_PRIORITY = 2;
    public static final int HIGHEST_PRIORITY = 3;

    // note types.
    public static final String OVERCLOCKING_NOTE_TYPE = "ocnt";
    public static final String PURPOSE_TYPE = "purt";
    public static final String BUILD_NOTE_TYPE = "bnt";


    // computer direction constants.
    public static final String DIRECTION_ABBREVIATION = "dir";
    public static final String COMPUTER_PART_ABBREVIATION = "cp";
    public static final String PURPOSE_ABBREVIATION = "pur";
    public static final String OVERCLOCKING_NOTE_ABBREVIATION = "ocn";
    public static final String BUILD_NOTE_ABBREVIATION = "bn";
    public static final String BUDGET_FIRST_DIRECTION_DESCRIPTION = "Set the components on to a surface, table, or ideally a surface that is anti-static.";
    public static final String BUDGET_SECOND_DIRECTION_DESCRIPTION = "Open both side panels on your computer case.";
    public static final String BUDGET_THIRD_DIRECTION_DESCRIPTION = "Take out any contents included in your computer case such as screws.";

    public static final String GAMING_FIRST_DIRECTION_DESCRIPTION = "Set the components on to a surface, table, or ideally a surface that is anti-static.";
    public static final String GAMING_SECOND_DIRECTION_DESCRIPTION = "Open the top panel of your computer case.";
    public static final String GAMING_THIRD_DIRECTION_DESCRIPTION = "Open the two side panels of your computer case.";
    public static final String GAMING_FOURTH_DIRECTION_DESCRIPTION = "Take out any contents included in your computer case such as screws and remove the stock fans too.";

    // overclocking note constants.
    public static final String BUDGET_OVERCLOCKING_LIST_NOTE_ONE = "I will be doing a minor overclock so turn on XMP to get higher ram speeds.";
    public static final String BUDGET_OVERCLOCKING_LIST_NOTE_TWO = "For the processor I will be aiming to do an undervolt while keeping a stock speed.";
    public static final String BUDGET_OVERCLOCKING_LIST_NOTE_THREE = "Although I am undervolting it is said that the processor will boost itself depending on work load.";

    public static final String GAMING_OVERCLOCKING_LIST_NOTE_ONE = "First turn on XMP for your memory/ram.";
    public static final String GAMING_OVERCLOCKING_LIST_NOTE_TWO = "Next just set a voltage that is higher by anywhere from .5v-1.0v but don't jump too much although you can.";
    public static final String GAMING_OVERCLOCKING_LIST_NOTE_THREE = "Once you get a stable build";

    // purpose list constants.
    public static final String BUDGET_PURPOSE_LIST_NOTE_ONE = "This build is meant to get at least 70-90 avg fps and a low fps of at least 30+";
    public static final String BUDGET_PURPOSE_LIST_NOTE_TWO = "This build is aimed at being able to do productivity and rendering videos at a decent pace.";
    public static final String BUDGET_PURPOSE_LIST_NOTE_THREE = "Another goal for this build is to be able to run some virtual machines but not heavily multi task since there are 6 cores on the processor.";

    public static final String GAMING_PURPOSE_LIST_NOTE_ONE = "This is a build aimed at getting high fps in games, at least 100+ average and a low of 60 fps.";
    public static final String GAMING_PURPOSE_LIST_NOTE_TWO = "This build is also aimed at using productivity and rendering videos quickly.";
    public static final String GAMING_PURPOSE_LIST_NOTE_THREE = "This build will also be able to use a lot of virtual machines because of the many cores on the processor.";

    // build note constants.
    public static final String BUDGET_BUILD_NOTE_LIST_NOTE_ONE = "Similar to the highest priced build make sure I find a stable BIOs version to avoid instability.";
    public static final String BUDGET_BUILD_NOTE_LIST_NOTE_TWO = "Make sure to monitor temperatures and voltages under the many workloads I will be using.";
    public static final String BUDGET_BUILD_NOTE_LIST_NOTE_THREE = "It is expected to get some minor hiccups but once the build is stable continue monitoring (voltages and temps) through the first month of the build.";

    public static final String GAMING_BUILD_NOTE_LIST_NOTE_ONE = "The first thing to note is that I will have to make sure to find a stable BIOs to avoid BSOD issues.";
    public static final String GAMING_BUILD_NOTE_LIST_NOTE_TWO = "Another important note is that I must also make sure to monitor the temperatures for this build.";
    public static final String GAMING_BUILD_NOTE_LIST_NOTE_THREE = "The third is that I definitely want to watch how the voltage fluctuates under certain work loads.";

    // computer part constants.
    public static final String GAMING_COMPUTER_PART_NAME_ONE = "Gigabyte x570 Master Motherboard";
    public static final double GAMING_COMPUTER_PART_PRICE_ONE = 359.99;
    public static final String GAMING_COMPUTER_PART_PURCHASE_LOCATION_ONE = "Amazon";
    public static final String GAMING_COMPUTER_PART_OTHER_NOTES_ONE = "This is a high tier motherboard with a really good VRM but it may not be needed because of lack of manual overclocking.";
    public static final String GAMING_COMPUTER_PART_PURCHASE_DATE_ONE = "2019-08-02";

    public static final String GAMING_COMPUTER_PART_NAME_TWO = "Ryzen 7 3700x";
    public static final double GAMING_COMPUTER_PART_PRICE_TWO = 329.99;
    public static final String GAMING_COMPUTER_PART_PURCHASE_LOCATION_TWO = "Best Buy";
    public static final String GAMING_COMPUTER_PART_OTHER_NOTES_TWO = "This is a good processor for light gaming, productivity tasks, and using VMs because of the 8 cores and 16 threads.";
    public static final String GAMING_COMPUTER_PART_PURCHASE_DATE_TWO = "2019-08-04";

    public static final String GAMING_COMPUTER_PART_NAME_THREE = "G.SKILL TridentZ RGB Series 32GB";
    public static final double GAMING_COMPUTER_PART_PRICE_THREE = 289.99;
    public static final String GAMING_COMPUTER_PART_PURCHASE_LOCATION_THREE = "Newegg";
    public static final String GAMING_COMPUTER_PART_OTHER_NOTES_THREE = "This RAM is expected to very pretty well as it has been tested and can reach 3200mhz, CL14 and it is a samsung b-die which is good on Ryzen motherboards. Since it is a high performing ram kit it is expected to be OOS (Out Of Stock).";
    public static final String GAMING_COMPUTER_PART_PURCHASE_DATE_THREE = "2019-08-05";

    public static final String BUDGET_COMPUTER_PART_NAME_ONE = "Gigabyte x570 Gaming Motherboard";
    public static final double BUDGET_COMPUTER_PART_PRICE_ONE = 169.99;
    public static final String BUDGET_COMPUTER_PART_PURCHASE_LOCATION_ONE = "Newegg";
    public static final String BUDGET_COMPUTER_PART_OTHER_NOTES_ONE = "This is a mid tier motherboard with a decent VRM and has a decent QVL (Qualified Vendor List) for the memory/ram.";
    public static final String BUDGET_COMPUTER_PART_PURCHASE_DATE_ONE = "2019-08-02";

    public static final String BUDGET_COMPUTER_PART_NAME_TWO = "Ryzen 5 3600x";
    public static final double BUDGET_COMPUTER_PART_PRICE_TWO = 249.99;
    public static final String BUDGET_COMPUTER_PART_PURCHASE_LOCATION_TWO = "Best Buy";
    public static final String BUDGET_COMPUTER_PART_OTHER_NOTES_TWO = "This is a good/decent processor for light gaming and productivity tasks (but maybe not for mass VM usage?)";
    public static final String BUDGET_COMPUTER_PART_PURCHASE_DATE_TWO = "2019-08-02";

    public static final String BUDGET_COMPUTER_PART_NAME_THREE = "Corsair LPX 32gb (2x16) 3200mhz ";
    public static final double BUDGET_COMPUTER_PART_PRICE_THREE = 161.99;
    public static final String BUDGET_COMPUTER_PART_PURCHASE_LOCATION_THREE = "Frys";
    public static final String BUDGET_COMPUTER_PART_OTHER_NOTES_THREE = "This RAM is expected to work pretty well as it has been tested and can reach 3200mhz and it is a samsung e-die which is good on x570 motherboards.";
    public static final String BUDGET_COMPUTER_PART_PURCHASE_DATE_THREE = "2019-08-02";

    // computer build errors.
    public static final String COMPUTER_BUILD_DOES_NOT_EXIST = "the computer build doesn't exist.";
    public static final String COMPUTER_BUILD_CANNOT_BE_MODIFIED = "you are not the owner of this computer build cannot modify it.";

    // build note errors.
    public static final String INVALID_BUILD_NOTE = "the note identifier does not exist.";
    public static final String BUILD_NOTE_CANNOT_BE_DELETED = "this build note cannot be deleted.";
    public static final String BUILD_NOTE_CANNOT_BE_UPDATED = "this build note cannot be updated.";

    // direction errors.
    public static final String INVALID_DIRECTION = "the direction identifier does not exist.";
    public static final String DIRECTION_CANNOT_BE_DELETED = "this direction cannot be deleted.";
    public static final String DIRECTION_CANNOT_BE_UPDATED = "this direction cannot be updated.";

    // overclocking note errors.
    public static final String INVALID_OVERCLOCKING_NOTE = "the overclocking note does not exist.";
    public static final String OVERCLOCKING_NOTE_CANNOT_BE_DELETED = "this overclocking note cannot be deleted.";
    public static final String OVERCLOCKING_NOTE_CANNOT_BE_UPDATED = "this overclocking note cannot be updated.";

    // purpose errors.
    public static final String INVALID_PURPOSE = "the purpose does not exist.";
    public static final String PURPOSE_CANNOT_BE_DELETED = "this purpose cannot be deleted.";
    public static final String PURPOSE_CANNOT_BE_UPDATED = "this purpose cannot be updated.";

    // computer part errors.
    public static final String INVALID_COMPUTER_PART = "the computer part identifier does not exist.";
    public static final String COMPUTER_PART_CANNOT_BE_DELETED = "this computer part cannot be deleted.";
    public static final String COMPUTER_PART_CANNOT_BE_UPDATED = "this computer part cannot be updated.";

    // build note success messages.
    public static final String BUILD_NOTE_DELETE_MESSAGE = "Build note was deleted successfully.";

    // direction success messages.
    public static final String DIRECTION_DELETE_MESSAGE = "Direction was deleted successfully.";

    // overclocking note success messages.
    public static final String OVERCLOCKING_NOTE_DELETE_MESSAGE = "Direction was deleted successfully.";

    // purpose success messages.
    public static final String PURPOSE_DELETE_MESSAGE = "Purpose was deleted successfully.";

    // computer part success messages.
    public static final String COMPUTER_PART_DELETE_MESSAGE = "Computer part was deleted successfully.";
}
