package tran.compbuildbackend.controllers.computerbuild;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tran.compbuildbackend.domain.computerbuild.BuildNote;
import tran.compbuildbackend.payload.computerbuild.ComputerBuildDetailResponse;
import tran.compbuildbackend.services.computerbuild.BuildNoteService;
import tran.compbuildbackend.validator.MapValidationErrorService;

import javax.validation.Valid;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.BUILD_NOTE_DELETE_MESSAGE;
import static tran.compbuildbackend.constants.mapping.MappingConstants.*;

@RestController
@RequestMapping(BUILD_NOTE_API)
public class BuildNoteController {

    private BuildNoteService buildNoteService;
    private MapValidationErrorService mapValidationErrorService;

    public BuildNoteController(BuildNoteService buildNoteService, MapValidationErrorService mapValidationErrorService) {
        this.buildNoteService = buildNoteService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping(BUILD_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> createBuildNote(@Valid @RequestBody BuildNote buildNote, BindingResult bindingResult,
                                             @PathVariable String buildIdentifier) {
        mapValidationErrorService.outputCustomError(bindingResult);

        // no errors so create the build note.
        BuildNote createdBuildNote = buildNoteService.create(buildIdentifier, buildNote);

        return new ResponseEntity<>(createdBuildNote, HttpStatus.CREATED);
    }

    @PatchMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> updateBuildNote(@Valid @RequestBody BuildNote newBuildNote, BindingResult bindingResult,
                                             @PathVariable String uniqueIdentifier) {
        mapValidationErrorService.outputCustomError(bindingResult);

        // no errors so update the build note.
        BuildNote updatedBuildNote = buildNoteService.update(newBuildNote, uniqueIdentifier);
        return new ResponseEntity<>(updatedBuildNote, HttpStatus.OK);
    }

    @DeleteMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> deleteBuildNote(@PathVariable String uniqueIdentifier) {
        buildNoteService.delete(uniqueIdentifier);

        // no errors at this point with deletion so return a success message.
        return new ResponseEntity<>(new ComputerBuildDetailResponse(BUILD_NOTE_DELETE_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> getBuildNoteByIdentifier(@PathVariable String uniqueIdentifier) {
        // get the build note and return its contents in the response.
        BuildNote buildNote = buildNoteService.getFromUniqueIdentifier(uniqueIdentifier);

        return new ResponseEntity<>(buildNote, HttpStatus.OK);
    }
}
