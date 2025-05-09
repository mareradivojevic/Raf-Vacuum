package rs.raf.nwpdomaci.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.nwpdomaci.dto.UpdateDto;
import rs.raf.nwpdomaci.dto.UserDto;
import rs.raf.nwpdomaci.model.User;
import rs.raf.nwpdomaci.services.UserService;
import rs.raf.nwpdomaci.utils.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController (UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAnyAuthority('can_read_users')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(200).body(userService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('can_create_users')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {

        String email = userDto.getEmail();
        Optional<User> optionalUser = this.userService.findByEmail(email);
        if(optionalUser.isPresent()) {
            System.err.println("A user with email: "+email+" already exists, cannot create user.");
            return ResponseEntity.status(400).body("A user with email: "+email+" already exists, cannot create user.");
        }

        String encodedPassword = this.passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        return ResponseEntity.ok(this.userService.create(userDto));
    }

    @PreAuthorize("hasAuthority('can_update_users')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UpdateDto updateDto) {

        Optional<User> optionalUser = this.userService.findById(updateDto.getId());
        if(optionalUser.isEmpty()) {
            System.err.println("User with id: "+updateDto.getId()+"not found.");
            return ResponseEntity.status(404).body("User with id: "+updateDto.getId()+" not found.");
        }

        String email = updateDto.getEmail();

        if(email == null || email.isEmpty()) {
            System.err.println("Email mustn't be empty or null.");
            return ResponseEntity.status(400).body("Email mustn't be empty or null.");
        }

        User loadedUser = optionalUser.get();
        String loadedEmail = loadedUser.getEmail();
        String loadedPassword = loadedUser.getPassword();

        if(!email.equals(loadedEmail)) {
            Optional<User> optionalUserByEmail = this.userService.findByEmail(email);
            if(optionalUserByEmail.isPresent()) {
                System.err.println("A user with email: "+email+" already exists, cannot update user.");
                return ResponseEntity.status(400).body("A user with email: "+email+" already exists, cannot update user.");
            }
        }

        updateDto.setPassword(loadedPassword);

        return ResponseEntity.ok(this.userService.update(updateDto));
    }

    @PreAuthorize("hasAuthority('can_delete_users')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

        Optional<User> optionalUser = this.userService.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getPermissions().clear();

            this.userService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(404).body("User not found.");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<User> optionalUser = this.userService.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.status(404).body("User with id: "+id+" not found.");
    }

}
