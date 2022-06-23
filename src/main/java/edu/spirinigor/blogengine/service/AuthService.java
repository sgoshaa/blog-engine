package edu.spirinigor.blogengine.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class AuthService {
    public NoAuthCheckResponse authCheck() {
        NoAuthCheckResponse noAuthCheckResponse = new NoAuthCheckResponse();
        noAuthCheckResponse.setResult(false);
        return noAuthCheckResponse;
    }

    public void getCaptcha() {
        Cage cage = new YCage();

        String name = "captcha.png";
        String result = "data:image/png;base64, ";
        try (OutputStream os = new FileOutputStream(name, false)) {
            cage.draw(cage.getTokenGenerator().next(), os);

            File file = new File(name);
            BufferedImage originalImage = ImageIO.read(file);
            BufferedImage bufferedImage = resizeImage(originalImage, 100, 35);

            File output = new File("name.png");
            ImageIO.write(bufferedImage, "PNG", output);
            String s = generatedBase64(output.getAbsolutePath());
            result += s;
            System.out.println(result);

           // Files.deleteIfExists(Path.of(String.valueOf(output)));
           // Files.deleteIfExists(Path.of(String.valueOf(file)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    private String generatedBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
