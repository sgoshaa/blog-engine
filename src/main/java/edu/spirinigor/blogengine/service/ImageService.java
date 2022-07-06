package edu.spirinigor.blogengine.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import edu.spirinigor.blogengine.dto.ErrorImageDto;
import edu.spirinigor.blogengine.dto.ErrorsCreatingPostDto;
import edu.spirinigor.blogengine.dto.ImageDto;
import edu.spirinigor.blogengine.exception.ImageException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private final String DIRECTORY_NAME = "upload";

    public String uploadImage(MultipartFile file) {
        checkImageFormat(file.getContentType());
        Path copyLocation = null;
        try {
            String dir = createDir(DIRECTORY_NAME);
            copyLocation = Paths
                    .get(dir + File.separator + getRandomString(9) + ".jpg");
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyLocation.toString();
    }

    private String getRandomString(int count) {
        String s = RandomStringUtils.randomAlphanumeric(count);
        return s.toLowerCase();
    }

    private String createDir(String dir) throws IOException {
        Path parent = Paths.get(dir);
        if (!Files.exists(parent)) {
            Files.createDirectory(parent);
        }
        String folder = dir;
        Path path;
        for (int i = 0; i < 3; i++) {
            path = Paths.get(folder + File.separator + getRandomString(2));
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            folder = path.toString();
        }
        return folder;
    }

    private void checkImageFormat(String format) {
        if (!format.equals("image/png") || !format.equals("image/jpeg")) {
            throw new ImageException("Изображение должно быть png или jpg");
        }
    }
}
