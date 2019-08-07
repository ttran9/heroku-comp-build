package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tran.compbuildbackend.domain.computerbuild.Purpose;

import static tran.compbuildbackend.constants.tests.TestUtility.COMPUTER_BUILD_ID_KEY;

public class PurposeUtility {
    public static String getPurposeAsJson(int priority, String description) {
        Purpose purpose = new Purpose();
        purpose.setDescription(description);
        purpose.setPriority(priority);
        return new Gson().toJson(purpose);
    }

    public static String getPurposeAsJson(int priority, String description, String computerBuildIdentifier, Long id,
                                            String purposeUniqueIdentifier) {
        Purpose purpose = createPurpose(priority, description);
        purpose.setId(id);
        purpose.setUniqueIdentifier(purposeUniqueIdentifier);

        String contents = new Gson().toJson(purpose);
        JsonObject jsonObject = new JsonParser().parse(contents).getAsJsonObject();
        jsonObject.addProperty(COMPUTER_BUILD_ID_KEY, computerBuildIdentifier);
        return jsonObject.toString();
    }

    public static Purpose createPurpose(int priority, String description) {
        Purpose purpose = new Purpose();
        purpose.setPriority(priority);
        purpose.setDescription(description);
        return purpose;
    }
}
