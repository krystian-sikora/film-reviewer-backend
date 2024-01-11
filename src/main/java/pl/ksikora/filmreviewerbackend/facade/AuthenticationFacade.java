package pl.ksikora.filmreviewerbackend.facade;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.ksikora.filmreviewerbackend.entity.UserEntity;
import pl.ksikora.filmreviewerbackend.repository.UserRepository;

@Component
@AllArgsConstructor
public class AuthenticationFacade implements IAuthenticationFacade {

    public final UserRepository userRepository;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserEntity getCurrentUser() {
        return userRepository.findByEmail(getAuthentication().getName()).orElseThrow();
    }
}
