package edu.spirinigor.blogengine.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import edu.spirinigor.blogengine.api.response.CaptchaResponse;
import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import edu.spirinigor.blogengine.model.CaptchaCode;
import edu.spirinigor.blogengine.repository.CaptchaCodeRepository;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
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
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AuthService {

    public static final String NAME = "captcha.png";
    private static final int TARGET_WIDTH = 100;
    private static final int TARGET_HEIGHT = 35;
    private final CaptchaCodeRepository captchaCodeRepository;

    public AuthService(CaptchaCodeRepository captchaCodeRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
    }

    public NoAuthCheckResponse authCheck() {
        NoAuthCheckResponse noAuthCheckResponse = new NoAuthCheckResponse();
        noAuthCheckResponse.setResult(false);
        return noAuthCheckResponse;
    }
    @Transactional
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
        return  createCaptchaResponse(result, secretCode);
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
        BufferedImage bufferedImage = resizeImage(originalImage, TARGET_WIDTH, TARGET_HEIGHT);
        ImageIO.write(bufferedImage, "PNG", file);
        return generatedBase64(file.getAbsolutePath());
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth
                , targetHeight, Scalr.OP_ANTIALIAS);
    }

    private String generatedBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private String generatedSecretCode(String code) {
        return Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8));
    }
}
