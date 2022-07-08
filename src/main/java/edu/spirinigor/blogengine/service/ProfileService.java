package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.ProfileRequestDto;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.util.UserUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Response editingMyProfile(ProfileRequestDto profileRequestDto) {
        User currentUser = UserUtils.getCurrentUser();
        if (profileRequestDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(profileRequestDto.getPassword()));
        }
        currentUser.setEmail(profileRequestDto.getEmail());
        currentUser.setName(profileRequestDto.getName());
        userRepository.save(currentUser);
        Response response = new Response();
        response.setResult(true);
        return response;
    }
}
