package edu.spirinigor.blogengine.util;

import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserUtils {

    private static UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Integer getIdCurrentUser() {
        return getCurrentUser().getId();
    }

    public static User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        User user = userOptional.get();
        return user;
    }
}
