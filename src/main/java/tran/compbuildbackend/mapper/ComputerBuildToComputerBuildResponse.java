package tran.compbuildbackend.mapper;

import tran.compbuildbackend.payload.computerbuild.ComputerBuildResponse;

public class ComputerBuildToComputerBuildResponse {
    public static ComputerBuildResponse buildToBuildResponseWithFields(String computerName, String buildIdentifier,
                                                                       String buildDescription) {
        ComputerBuildResponse computerBuildResponse = new ComputerBuildResponse();
        computerBuildResponse.setName(computerName);
        computerBuildResponse.setBuildIdentifier(buildIdentifier);
        computerBuildResponse.setBuildDescription(buildDescription);

        return computerBuildResponse;
    }
}
