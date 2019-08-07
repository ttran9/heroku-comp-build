package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;

public class ComputerBuildUtility {
    public static String getComputerBuildAsJson(String computerBuildName, String buildDescription) {
        ComputerBuild computerBuild = new ComputerBuild();

        computerBuild.setName(computerBuildName);
        computerBuild.setBuildDescription(buildDescription);

        return new Gson().toJson(computerBuild);
    }

    public static ComputerBuild createComputerBuild(String computerBuildName, String buildDescription) {
        ComputerBuild computerBuild = new ComputerBuild();
        computerBuild.setName(computerBuildName);
        computerBuild.setBuildDescription(buildDescription);
        return computerBuild;
    }

}
