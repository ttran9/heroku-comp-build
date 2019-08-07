package tran.compbuildbackend.controllers.computerbuild;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.dto.computerbuild.ComputerBuildDto;
import tran.compbuildbackend.mapper.ComputerBuildToComputerBuildResponse;
import tran.compbuildbackend.payload.computerbuild.ComputerBuildResponse;
import tran.compbuildbackend.services.computerbuild.ComputerBuildService;
import tran.compbuildbackend.services.computerbuild.mapper.ComputerBuildDtoMapper;
import tran.compbuildbackend.validator.MapValidationErrorService;

import javax.validation.Valid;

import static tran.compbuildbackend.constants.mapping.MappingConstants.*;

@RestController
@RequestMapping(COMPUTER_BUILD_API)
public class ComputerBuildController {

    private MapValidationErrorService mapValidationErrorService;

    private ComputerBuildService computerBuildService;

    private ComputerBuildDtoMapper computerBuildDtoMapper;

    public ComputerBuildController(MapValidationErrorService mapValidationErrorService, ComputerBuildService computerBuildService,
                                   ComputerBuildDtoMapper computerBuildDtoMapper) {
        this.mapValidationErrorService = mapValidationErrorService;
        this.computerBuildService = computerBuildService;
        this.computerBuildDtoMapper = computerBuildDtoMapper;
    }

    @PostMapping
    public ResponseEntity<?> createComputerBuild(@RequestBody @Valid ComputerBuild computerBuild, BindingResult bindingResult) {
        mapValidationErrorService.outputCustomError(bindingResult);

        ComputerBuild newComputerBuild = computerBuildService.createNewComputerBuild(computerBuild);
        ComputerBuildResponse response = ComputerBuildToComputerBuildResponse.buildToBuildResponseWithFields
                (newComputerBuild.getName(), newComputerBuild.getBuildIdentifier(), newComputerBuild.getBuildDescription());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(BUILD_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> deleteComputerBuild(@PathVariable String buildIdentifier) {
        computerBuildService.deleteComputerBuild(buildIdentifier);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(BUILD_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> getComputerBuildByIdentifier(@PathVariable String buildIdentifier) {
        ComputerBuild computerBuild = computerBuildService.getComputerBuildByBuildIdentifier(buildIdentifier);
        ComputerBuildDto computerBuildDto = computerBuildDtoMapper.computerBuildToComputerBuildDto(computerBuild);
        return new ResponseEntity<>(computerBuildDto, HttpStatus.OK);
    }

    @GetMapping
    public Iterable<ComputerBuildDto> getAllComputerBuilds() {
        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuilds();
        return computerBuildDtoMapper.computerBuildsToComputerBuildDtos(computerBuilds);
    }

    @GetMapping(USER_NAME_REQUEST + USER_NAME_PATH_VARIABLE)
    public Iterable<ComputerBuildDto> getAllComputerBuildsByUser(@PathVariable String username) {
        Iterable<ComputerBuild> computerBuilds = computerBuildService.getAllComputerBuildsFromUser(username);
        return computerBuildDtoMapper.computerBuildsToComputerBuildDtos(computerBuilds);
    }
}
