package rs.raf.nwpdomaci.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.nwpdomaci.model.User;
import rs.raf.nwpdomaci.model.Vacuum;
import rs.raf.nwpdomaci.model.VacuumStatus;
import rs.raf.nwpdomaci.services.UserService;
import rs.raf.nwpdomaci.services.VacuumService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/vacuums")
public class VacuumController {

    private final VacuumService vacuumService;
    private final UserService userService;

    @Autowired
    public VacuumController(VacuumService vacuumService, UserService userService) {
        this.vacuumService = vacuumService;
        this.userService = userService;
    }


    @PreAuthorize(value = "hasAuthority('can_add_vacuum')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacuum> addVacuum(@RequestParam String name) {

        Vacuum vacuum= this.vacuumService.createVacuum(name);
        User user = this.getUserFromContext();
        if(user == null) {
            System.err.println("Context error: unable to find user");
            return ResponseEntity.internalServerError().build();
        }

        vacuum.setUser(user);

        List<Vacuum> vacuums = user.getVacuums();
        vacuums.add(vacuum);
        this.userService.save(user);

        return ResponseEntity.ok(vacuum);
    }


    @PreAuthorize(value = "hasAnyAuthority('can_search_vacuum')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Vacuum>> searchVacuums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date dateTo) {

        User user = this.getUserFromContext();

        if(user == null) {
            System.err.println("Context error: unable to find user");
            return ResponseEntity.internalServerError().build();
        }

        List<Vacuum> vacuums = this.vacuumService.searchVacuums(user.getId(), name, status, dateFrom, dateTo);
        return ResponseEntity.ok(vacuums);
    }

    @PreAuthorize(value = "hasAuthority('can_remove_vacuum')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeVacuum(@PathVariable Integer id) {
        User user = getUserFromContext();
        if(user == null) {
            System.err.println("Context error: unable to find user");
            return ResponseEntity.internalServerError().build();
        }

        for(Vacuum vacuum: user.getVacuums()) {
            if(id.equals(vacuum.getId())) {

                if(vacuum.getChangingStatus())
                    return ResponseEntity.status(409).body("Cannot remove vacuum because another operation is being performed");

                if(vacuum.getStatus().equals(VacuumStatus.STOPPED)) {
                    vacuum.setActive(false);

                    this.userService.save(user);
                    return ResponseEntity.ok().build();
                }

                return ResponseEntity.status(409).body("Vacuum cannot be removed because it is in state: "+vacuum.getStatus());
            }
        }

        return ResponseEntity.status(404).body("Vacuum not found.");
    }

    @PreAuthorize("hasAuthority('can_start_vacuum')")
    @GetMapping("/start/{id}")
    public ResponseEntity<?> startVacuum(@PathVariable Integer id) {
        Optional<Vacuum> optionalVacuum = this.vacuumService.findById(id);
        if(optionalVacuum.isEmpty())
            return ResponseEntity.status(404).body("Vacuum with id: "+id+" not found");

        Vacuum vacuum = optionalVacuum.get();
        if(vacuum.getChangingStatus())
            return ResponseEntity.status(409).body("Cannot start vacuum because another operation is being performed");

        if(!vacuum.getStatus().equals(VacuumStatus.STOPPED))
            return ResponseEntity.status(409).body("Cannot start vacuum in state: "+vacuum.getStatus());

        this.vacuumService.startVacuum(vacuum);
        return ResponseEntity.ok().build();
    }



    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = this.userService.findByEmail(authentication.getName());
        if(optionalUser.isPresent())
            return optionalUser.get();

        return null;
    }

}
