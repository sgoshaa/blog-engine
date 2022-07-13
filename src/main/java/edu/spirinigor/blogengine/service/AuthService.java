package edu.spirinigor.blogengine.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.api.request.LoginRequest;
import edu.spirinigor.blogengine.api.request.PasswordRecoveryRequest;
import edu.spirinigor.blogengine.api.response.CaptchaResponse;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.api.response.LoginResponse;
import edu.spirinigor.blogengine.api.response.LogoutResponse;
import edu.spirinigor.blogengine.api.response.UserLoginResponse;
import edu.spirinigor.blogengine.dto.ErrorsCreatingUserDto;
import edu.spirinigor.blogengine.exception.AnyException;
import edu.spirinigor.blogengine.mapper.UserMapper;
import edu.spirinigor.blogengine.model.CaptchaCode;
import edu.spirinigor.blogengine.repository.CaptchaCodeRepository;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.util.ImageUtils;
import edu.spirinigor.blogengine.util.UserUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    public static final String NAME = "captcha.png";
    private static final int TARGET_WIDTH = 300;
    private static final int TARGET_HEIGHT = 100;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageUtils imageUtils;
    @Value("${blog.main-link}")
    private String MAIN_LINK;

    public AuthService(CaptchaCodeRepository captchaCodeRepository, UserRepository userRepository,
                       UserMapper userMapper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, PostRepository postRepository, ImageUtils imageUtils) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.imageUtils = imageUtils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = (User) authenticate.getPrincipal();

        return getLoginResponse(user.getUsername());
    }

    public LoginResponse authCheck(Principal principal) {
        if (principal == null) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setResult(false);
            return loginResponse;
        }
        return getLoginResponse(principal.getName());
    }

    public LogoutResponse logout() {
        SecurityContextHolder.clearContext();
        return new LogoutResponse();
    }

    public CaptchaResponse getCaptcha() {
        Cage cage = new YCage();
        String result = "data:image/png;base64, ";
        OutputStream os = null;
        String secretCode = null;
        String code = null;
        try {
            os = new FileOutputStream(NAME, false);
            code = cage.getTokenGenerator().next();
            cage.draw(code, os);
            secretCode = generatedSecretCode(code);
            result += getImageAsString(NAME);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                Files.deleteIfExists(Path.of(NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveToDatabase(code, secretCode);
        return createCaptchaResponse(result, secretCode);
    }

    @Transactional
    public Response createUser(CreateUserRequest userDto) {
        Response userResponse = new Response();
        if (isCorrectEmail(userDto.getEmail())
                && isCorrectCaptcha(userDto.getCaptcha(), userDto.getCaptchaSecret())
                && isCorrectName(userDto.getName())
                && UserUtils.isCorrectPassword(userDto.getPassword())
        ) {
            userResponse.setResult(true);
            edu.spirinigor.blogengine.model.User user = userMapper.dtoToUser(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return userResponse;
        }
        userResponse.setResult(false);
        userResponse.setErrors(checkUser(userDto));
        return userResponse;
    }

    @Transactional
    public Response changePassword(PasswordRecoveryRequest passwordRecoveryRequest) {
        Optional<edu.spirinigor.blogengine.model.User> userOptional = userRepository
                .findByCode(passwordRecoveryRequest.getCode());
        Map<String, String> map = checkParametersForPasswordRecovery(passwordRecoveryRequest, userOptional);
        Response response = new Response();
        if (!map.isEmpty()) {
            response.setResult(false);
            response.setErrors(map);
            return response;
        }
        edu.spirinigor.blogengine.model.User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(passwordRecoveryRequest.getPassword()));
        user.setCode(null);
        userRepository.save(user);
        response.setResult(true);
        return response;
    }

    private Map<String, String> checkParametersForPasswordRecovery(
            PasswordRecoveryRequest passwordRecoveryRequest,
            Optional<edu.spirinigor.blogengine.model.User> userOptional) {
        HashMap<String, String> errors = new HashMap<>();

        if (!isCorrectCode(userOptional, passwordRecoveryRequest.getCode())) {
            errors.put("code", "\"Ссылка для восстановления пароля устарела." +
                    "<a href=\"" + MAIN_LINK + "/auth/restore\">Запросить ссылку снова</a>\"");
        }
        if (!isCorrectCaptcha(passwordRecoveryRequest.getCaptcha(), passwordRecoveryRequest.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        if (!UserUtils.isCorrectPassword(passwordRecoveryRequest.getPassword())) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        return errors;
    }

    private Boolean isCorrectCode(Optional<edu.spirinigor.blogengine.model.User> user, String code) {
        if (user.isEmpty() || !user.get().getCode().equals(code)) {
            return false;
        }
        return true;
    }

    private LoginResponse getLoginResponse(String email) {
        edu.spirinigor.blogengine.model.User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AnyException("Пользователь с таким " + email + " не найден"));
        UserLoginResponse userLoginResponse = userMapper.toUserLoginResponse(currentUser);
        if (currentUser.getIsModerator() == 1) {
            long count = postRepository.findAllByStatusNew().size();
            userLoginResponse.setModerationCount((int) count);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }

    private Boolean isCorrectEmail(String email) {
        return userRepository.findByEmail(email).orElse(null) == null;
    }

    private Boolean isCorrectName(String name) {
        Pattern pattern = Pattern.compile("[^!@#$%^&*()_]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private Boolean isCorrectCaptcha(String captcha, String secretCode) {
        CaptchaCode bySecretCode = captchaCodeRepository.findBySecretCode(secretCode).get();
        return bySecretCode.getCode().equals(captcha);
    }

    private ErrorsCreatingUserDto checkUser(CreateUserRequest userDto) {
        ErrorsCreatingUserDto errorsCreatingUserDto = new ErrorsCreatingUserDto();
        if (!isCorrectEmail(userDto.getEmail())) {
            errorsCreatingUserDto.setEmail("Этот e-mail уже зарегистрирован");
        }
        if (!isCorrectName(userDto.getName())) {
            errorsCreatingUserDto.setName("Имя указано неверно, использованы спец.символы: ! @ # $ % ^ & * () _");
        }
        if (!isCorrectCaptcha(userDto.getCaptcha(), userDto.getCaptchaSecret())) {
            errorsCreatingUserDto.setCaptcha("Код с картинки введён неверно");
        }
        if (!UserUtils.isCorrectPassword(userDto.getPassword())) {
            errorsCreatingUserDto.setPassword("Пароль короче 6-ти символов");
        }
        return errorsCreatingUserDto;
    }

    private CaptchaResponse createCaptchaResponse(String result, String secretCode) {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setImage(result);
        captchaResponse.setSecret(secretCode);
        return captchaResponse;
    }

    private void saveToDatabase(String code, String secretCode) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(LocalDateTime.now());
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secretCode);
        captchaCodeRepository.save(captchaCode);
    }

    private String getImageAsString(String name) throws Exception {
        File file = new File(name);
        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage bufferedImage = imageUtils.resizeImage(originalImage, TARGET_WIDTH, TARGET_HEIGHT);
        ImageIO.write(bufferedImage, "PNG", file);
        return generatedBase64(file.getAbsolutePath());
    }

    private String generatedBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private String generatedSecretCode(String code) {
        return Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8));
    }
}
