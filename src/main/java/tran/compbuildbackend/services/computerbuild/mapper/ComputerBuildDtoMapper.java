package tran.compbuildbackend.services.computerbuild.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import tran.compbuildbackend.domain.computerbuild.*;
import tran.compbuildbackend.dto.computerbuild.ComputerBuildDto;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that provides methods to map a collection of ComputerBuild objects to ComputerBuildDto objects and
 * a ComputerBuild object to a ComputerBuildDto.
 */
@Component
public class ComputerBuildDtoMapper {

    private ModelMapper modelMapper;

    public ComputerBuildDtoMapper() {
        modelMapper = new ModelMapper();
    }

    public ComputerBuildDto computerBuildToComputerBuildDto(ComputerBuild computerBuild) {
        return addFieldsToComputerBuildDto(computerBuild);

    }

    public Iterable<ComputerBuildDto> computerBuildsToComputerBuildDtos(Iterable<ComputerBuild> computerBuilds) {
        List<ComputerBuildDto> computerBuildDtos = new LinkedList<>();
        computerBuilds.forEach(computerBuild -> {
            ComputerBuildDto computerBuildDto = addFieldsToComputerBuildDto(computerBuild);
            computerBuildDtos.add(computerBuildDto);
        });
        return computerBuildDtos;
    }

    private ComputerBuildDto addFieldsToComputerBuildDto(ComputerBuild computerBuild) {
        ComputerBuildDto computerBuildDto = modelMapper.map(computerBuild, ComputerBuildDto.class);
        computerBuildDto.getBuildNotes().sort(Comparator.comparing(BuildNote::getPriority).reversed());
        computerBuildDto.getOverclockingNotes().sort(Comparator.comparing(OverclockingNote::getPriority).reversed());
        computerBuildDto.getPurposeList().sort(Comparator.comparing(Purpose::getPriority).reversed());
        computerBuildDto.setUsername(computerBuild.getUser().getUsername());
        return computerBuildDto;
    }
}
