package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tran.compbuildbackend.domain.computerbuild.Direction;

import static tran.compbuildbackend.constants.tests.TestUtility.COMPUTER_BUILD_ID_KEY;

public class DirectionUtility {
    public static String getDirectionAsJson(String description) {
        Direction direction = new Direction();
        direction.setDescription(description);
        return new Gson().toJson(direction);
    }

    public static String getDirectionAsJson(String description, String computerBuildIdentifier, Long id,
                                            String directionUniqueIdentifier) {
        Direction direction = new Direction();
        direction.setDescription(description);
        direction.setId(id);
        direction.setUniqueIdentifier(directionUniqueIdentifier);

        String contents =  new Gson().toJson(direction);
        JsonObject jsonObject = new JsonParser().parse(contents).getAsJsonObject();
        jsonObject.addProperty(COMPUTER_BUILD_ID_KEY, computerBuildIdentifier);
        return jsonObject.toString();

    }

    public static Direction createDirection(String description) {
        Direction direction = new Direction();
        direction.setDescription(description);
        return direction;
    }
}
