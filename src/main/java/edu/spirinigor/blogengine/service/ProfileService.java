package edu.spirinigor.blogengine.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import edu.spirinigor.blogengine.api.request.ProfileRequestDto;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.util.ImageUtils;
import edu.spirinigor.blogengine.util.UserUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

@Service
public class ProfileService {

    public static final long MAX_SIZE_PHOTO = 5242880;
    public static final int SIZE_AVATAR = 36;
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
    public Response editingMyProfile(ProfileRequestDto profileRequestDto, HttpServletRequest request) {
        User currentUser = UserUtils.getCurrentUser();
        HashMap<String, String> errors = checkingProfileEditingRequest(profileRequestDto, currentUser);
        Response response = new Response();
        if (!errors.isEmpty()) {
            response.setErrors(errors);
            response.setResult(false);
            return response;
        }
        if (profileRequestDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(profileRequestDto.getPassword()));
        }
        if (profileRequestDto.getPhoto() != null && !profileRequestDto.getPhoto().equals("")) {
            String s = imageService.uploadImagesToCloudinary((MultipartFile) profileRequestDto.getPhoto(), SIZE_AVATAR,SIZE_AVATAR);
            currentUser.setPhoto(s);
        }
        if (profileRequestDto.getPhoto() != null && profileRequestDto.getPhoto().equals("")) {
            currentUser.setPhoto(null);
        }
        currentUser.setEmail(profileRequestDto.getEmail());
        currentUser.setName(profileRequestDto.getName());
        userRepository.save(currentUser);
        response.setResult(true);
        return response;
    }

    private String uploadingAvatar(HttpServletRequest request, MultipartFile photo) {
        String s = imageService.uploadImage(request, photo);
        try {
            String realPath = request.getServletContext().getRealPath(s);
            File file = new File(realPath);
            BufferedImage bufferedImage = imageUtils.resizeImage(ImageIO.read(file), SIZE_AVATAR, SIZE_AVATAR);
            file = new File(realPath);
            ImageIO.write(bufferedImage, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private HashMap<String, String> checkingProfileEditingRequest(ProfileRequestDto profileRequestDto, User currentUser) {
        HashMap<String, String> errors = new HashMap<>();
        if (profileRequestDto.getPassword() != null && !UserUtils.isCorrectPassword(profileRequestDto.getPassword())) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (profileRequestDto.getEmail() != null && !UserUtils.isCorrectEmail(profileRequestDto.getEmail())
                && !profileRequestDto.getEmail().equals(currentUser.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (profileRequestDto.getName() != null && !UserUtils.isCorrectName(profileRequestDto.getName())) {
            errors.put("name", "Имя указано неверно");
        }
        if (profileRequestDto.getPhoto() != null && !profileRequestDto.getPhoto().equals("")) {
            MultipartFile photo = (MultipartFile) profileRequestDto.getPhoto();
            if (photo.getSize() > MAX_SIZE_PHOTO) {
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
            }
        }
        return errors;
    }
}
