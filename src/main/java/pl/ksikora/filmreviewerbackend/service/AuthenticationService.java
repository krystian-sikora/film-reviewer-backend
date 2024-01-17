package pl.ksikora.filmreviewerbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ksikora.filmreviewerbackend.dto.AuthenticationRequest;
import pl.ksikora.filmreviewerbackend.dto.AuthenticationResponse;
import pl.ksikora.filmreviewerbackend.dto.RegistrationRequest;
import pl.ksikora.filmreviewerbackend.config.JwtService;
import pl.ksikora.filmreviewerbackend.entity.TokenEntity;
import pl.ksikora.filmreviewerbackend.exceptions.UserAlreadyExistsException;
import pl.ksikora.filmreviewerbackend.repository.TokenRepository;
import pl.ksikora.filmreviewerbackend.enums.TokenType;
import pl.ksikora.filmreviewerbackend.enums.Role;
import pl.ksikora.filmreviewerbackend.entity.UserEntity;
import pl.ksikora.filmreviewerbackend.repository.UserRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest request) throws UserAlreadyExistsException {
        var user = UserEntity.builder()
                .email(request.getEmail())
                .nickname(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }


        revokeAllUserTokens(user);
        String accessToken = jwtService.generateToken(user);
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader("AUTHORIZATION");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);

        if (userEmail == null) return;

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtService.isTokenValid(refreshToken, user)) {
            revokeAllUserTokens(user);
            String accessToken = jwtService.generateToken(user);
            saveUserToken(user, accessToken);

            var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
}
