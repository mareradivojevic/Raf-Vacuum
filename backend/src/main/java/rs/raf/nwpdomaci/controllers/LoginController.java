package rs.raf.nwpdomaci.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.nwpdomaci.model.User;
import rs.raf.nwpdomaci.requests.LoginRequest;
import rs.raf.nwpdomaci.responses.LoginResponse;
import rs.raf.nwpdomaci.services.UserService;
import rs.raf.nwpdomaci.utils.JwtUtil;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public LoginController (JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Bad credentials");
        }

        Optional<User> optionalUser = this.userService.findByEmail(loginRequest.getUsername());
        User user = null;
        if(optionalUser.isPresent())
            user = optionalUser.get();
        else
            return ResponseEntity.status(403).body("No such user");

        String jwt = this.jwtUtil.generateToken(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwt(jwt);

        return ResponseEntity.ok(loginResponse);
    }
}
