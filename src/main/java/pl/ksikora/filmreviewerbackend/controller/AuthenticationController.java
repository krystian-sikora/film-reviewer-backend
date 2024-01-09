package pl.ksikora.filmreviewerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ksikora.filmreviewerbackend.dto.AuthenticationRequest;
import pl.ksikora.filmreviewerbackend.dto.AuthenticationResponse;
import pl.ksikora.filmreviewerbackend.exceptions.UserAlreadyExistsException;
import pl.ksikora.filmreviewerbackend.service.AuthenticationService;
import pl.ksikora.filmreviewerbackend.dto.RegistrationRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws UserAlreadyExistsException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return Optional
                .ofNullable(service.authenticate(request))
                .map(token -> ResponseEntity.ok().body(token))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
