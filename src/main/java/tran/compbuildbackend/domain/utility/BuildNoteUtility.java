package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tran.compbuildbackend.domain.computerbuild.BuildNote;

import static tran.compbuildbackend.constants.tests.TestUtility.COMPUTER_BUILD_ID_KEY;

public class BuildNoteUtility {
    public static String getBuildNoteAsJson(int priority, String description) {
        BuildNote buildNote = createBuildNote(priority, description);

        return new Gson().toJson(buildNote);
    }

    public static String getBuildNoteAsJson(int priority, String description, String computerBuildIdentifier, Long id,
                                            String buildNoteUniqueIdentifier) {
        BuildNote buildNote = createBuildNote(priority, description);
        buildNote.setId(id);
        buildNote.setUniqueIdentifier(buildNoteUniqueIdentifier);

        String contents = new Gson().toJson(buildNote);
        JsonObject jsonObject = new JsonParser().parse(contents).getAsJsonObject();
        jsonObject.addProperty(COMPUTER_BUILD_ID_KEY, computerBuildIdentifier);
        return jsonObject.toString();
    }

    public static BuildNote createBuildNote(int priority, String description) {
        BuildNote buildNote = new BuildNote();
        buildNote.setDescription(description);
        buildNote.setPriority(priority);
        return buildNote;
    }


}
