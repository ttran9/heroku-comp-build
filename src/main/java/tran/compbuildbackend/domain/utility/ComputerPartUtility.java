package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tran.compbuildbackend.domain.computerbuild.ComputerPart;

import java.time.LocalDate;

import static tran.compbuildbackend.constants.tests.TestUtility.COMPUTER_BUILD_ID_KEY;
import static tran.compbuildbackend.constants.tests.TestUtility.PURCHASE_DATE_KEY;

public class ComputerPartUtility {
    public static String getComputerPartAsJson(String name, String date, String placePurchasedAt, String otherNotes,
                                               double price) {
        ComputerPart computerPart = setComputerPartFields(name, placePurchasedAt, otherNotes, price);

        String contents = new Gson().toJson(computerPart);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(contents).getAsJsonObject();
        jsonObject.addProperty(PURCHASE_DATE_KEY, date);
        return jsonObject.toString();
    }

    public static String getComputerPartAsJson(String name, String date, String placePurchasedAt, String otherNotes,
                                               double price, String computerBuildIdentifier, Long id,
                                               String computerPartUniqueIdentifier) {
        ComputerPart computerPart = setComputerPartFields(name, placePurchasedAt, otherNotes, price);
        computerPart.setId(id);
        computerPart.setUniqueIdentifier(computerPartUniqueIdentifier);

        String contents = new Gson().toJson(computerPart);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(contents).getAsJsonObject();
        jsonObject.addProperty(PURCHASE_DATE_KEY, date);
        jsonObject.addProperty(COMPUTER_BUILD_ID_KEY, computerBuildIdentifier);
        return jsonObject.toString();
    }

    private static ComputerPart setComputerPartFields(String name, String placePurchasedAt, String otherNotes, double price) {
        ComputerPart computerPart = new ComputerPart();
        computerPart.setName(name);
        computerPart.setPlacePurchasedAt(placePurchasedAt);
        computerPart.setPrice(price);
        computerPart.setOtherNote(otherNotes);
        return computerPart;
    }

    public static ComputerPart createComputerPart(String name, LocalDate localDate, String placePurchaesdAt, double price,
                                                  String otherNotes) {
        ComputerPart computerPart = new ComputerPart();
        computerPart.setName(name);
        computerPart.setPurchaseDate(localDate);
        computerPart.setPlacePurchasedAt(placePurchaesdAt);
        computerPart.setPrice(price);
        computerPart.setOtherNote(otherNotes);
        return computerPart;
    }
}
