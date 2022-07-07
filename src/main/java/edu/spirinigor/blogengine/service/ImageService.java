package edu.spirinigor.blogengine.service;

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

    private final int NUMBER_SUB_FOLDERS = 3;
    private final String ROOT_DIRECTORY_NAME = "upload";

    public String uploadImage(MultipartFile file) {
        Path copyLocation = null;
        try {
            assert file.getContentType() != null;
            String imageExtension = getImageExtension(file.getContentType());
            String dir = createDir(ROOT_DIRECTORY_NAME);
            copyLocation = Paths
                    .get(dir + File.separator + getRandomString(9) + imageExtension);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert copyLocation != null;
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
        for (int i = 0; i < NUMBER_SUB_FOLDERS; i++) {
            path = Paths.get(folder + File.separator + getRandomString(2));
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            folder = path.toString();
        }
        return folder;
    }

    private String getImageExtension(String imageType){
        if (imageType.equals("image/jpeg") || imageType.equals("image/jpg") ){
            return ".jpg";
        }else if (imageType.equals("image/png")){
            return ".png";
        }else {
            throw new ImageException("Изображение должно быть png или jpg");
        }
    }
}
