package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.ProfileRequestDto;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.util.ImageUtils;
import edu.spirinigor.blogengine.util.UserUtils;
import org.aspectj.weaver.loadtime.Options;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUtils imageUtils;
    private final ImageService imageService;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageUtils imageUtils, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageUtils = imageUtils;
        this.imageService = imageService;
    }

    @Transactional
    public Response editingMyProfile(ProfileRequestDto profileRequestDto) {
        User currentUser = UserUtils.getCurrentUser();
        if (profileRequestDto.getPassword() != null) {
            if (!UserUtils.isCorrectPassword(profileRequestDto.getPassword())) {

            }
            currentUser.setPassword(passwordEncoder.encode(profileRequestDto.getPassword()));
        }
        if (profileRequestDto.getPhoto() != null) {
            String s = uploadingAvatar(profileRequestDto.getPhoto());
            currentUser.setPhoto(s);
        }
        currentUser.setEmail(profileRequestDto.getEmail());
        currentUser.setName(profileRequestDto.getName());
        userRepository.save(currentUser);
        Response response = new Response();
        response.setResult(true);
        return response;
    }

    private String uploadingAvatar(MultipartFile photo) {
        String s = imageService.uploadImage(photo);
        try {
            File file = new File(s);
            BufferedImage bufferedImage = imageUtils.resizeImage(ImageIO.read(file), 36,36);
            file = new File(s);
            ImageIO.write(bufferedImage,"jpg",file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
