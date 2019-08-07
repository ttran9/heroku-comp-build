package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;

import static tran.compbuildbackend.constants.tests.TestUtility.COMPUTER_BUILD_ID_KEY;

public class OverclockingNoteUtility {
    public static String getOverclockingNoteAsJson(int priority, String description) {
        OverclockingNote overclockingNote = new OverclockingNote();

        overclockingNote.setPriority(priority);
        overclockingNote.setDescription(description);

        return new Gson().toJson(overclockingNote);
    }

    public static String getOverclockingNoteAsJson(int priority, String description, String computerBuildIdentifier, Long id,
                                          String purposeUniqueIdentifier) {
        OverclockingNote overclockingNote = createOverclockingNote(priority, description);
        overclockingNote.setId(id);
        overclockingNote.setUniqueIdentifier(purposeUniqueIdentifier);

        String contents = new Gson().toJson(overclockingNote);
        JsonObject jsonObject = new JsonParser().parse(contents).getAsJsonObject();
        jsonObject.addProperty(COMPUTER_BUILD_ID_KEY, computerBuildIdentifier);
        return jsonObject.toString();
    }

    public static OverclockingNote createOverclockingNote(int priority, String description) {
        OverclockingNote overclockingNote = new OverclockingNote();
        overclockingNote.setDescription(description);
        overclockingNote.setPriority(priority);
        return overclockingNote;
    }
}
