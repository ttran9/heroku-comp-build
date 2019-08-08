package tran.compbuildbackend.controllers.computerbuild;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;
import tran.compbuildbackend.payload.computerbuild.ComputerBuildDetailResponse;
import tran.compbuildbackend.services.computerbuild.OverclockingNoteService;
import tran.compbuildbackend.validator.MapValidationErrorService;

import javax.validation.Valid;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.OVERCLOCKING_NOTE_DELETE_MESSAGE;
import static tran.compbuildbackend.constants.mapping.MappingConstants.*;

@RestController
@RequestMapping(OVERCLOCKING_NOTE_API)
public class OverclockingNoteController {

    private OverclockingNoteService overclockingNoteService;

    private MapValidationErrorService mapValidationErrorService;

    public OverclockingNoteController(OverclockingNoteService overclockingNoteService, MapValidationErrorService mapValidationErrorService) {
        this.overclockingNoteService = overclockingNoteService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping(BUILD_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> createOverclockingNote(@Valid @RequestBody OverclockingNote overclockingNote, BindingResult bindingResult,
                                             @PathVariable String buildIdentifier) {
        mapValidationErrorService.outputCustomError(bindingResult);

        // no errors so create the overclocking note.
        OverclockingNote createdOverclockingNote = overclockingNoteService.create(buildIdentifier, overclockingNote);

        return new ResponseEntity<>(createdOverclockingNote, HttpStatus.CREATED);
    }

    @PatchMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> updateOverclockingNote(@Valid @RequestBody OverclockingNote newOverclockingNote, BindingResult bindingResult,
                                             @PathVariable String uniqueIdentifier) {
        mapValidationErrorService.outputCustomError(bindingResult);

        // no errors so update the overclocking note.
        OverclockingNote updatedOverclockingNote = overclockingNoteService.update(newOverclockingNote, uniqueIdentifier);
        return new ResponseEntity<>(updatedOverclockingNote, HttpStatus.OK);
    }

    @DeleteMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> deleteOverclockingNote(@PathVariable String uniqueIdentifier) {
        OverclockingNote deletedOverclockingNote = overclockingNoteService.delete(uniqueIdentifier);

        // no errors at this point with deletion so return a success message.
        return new ResponseEntity<>(new ComputerBuildDetailResponse(OVERCLOCKING_NOTE_DELETE_MESSAGE,
                deletedOverclockingNote.getUniqueIdentifier()), HttpStatus.OK);
    }

    @GetMapping(BUILD_IDENTIFIER_PATH_VARIABLE + URL_SEPARATOR + UNIQUE_IDENTIFIER_PATH_VARIABLE)
    public ResponseEntity<?> getOverclockingNoteByIdentifier(@PathVariable String uniqueIdentifier) {
        // get the overclocking note and return its contents in the response.
        OverclockingNote overclockingNote = overclockingNoteService.getFromUniqueIdentifier(uniqueIdentifier);

        return new ResponseEntity<>(overclockingNote, HttpStatus.OK);
    }


}
