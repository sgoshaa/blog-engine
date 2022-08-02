package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.exception.ImageException;
import edu.spirinigor.blogengine.util.UserUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    public String uploadImage(HttpServletRequest request, MultipartFile file) {
        String dir = "";
        try {
            assert file.getContentType() != null;
            String imageExtension = getImageExtension(file.getContentType());
            Pair<String, String> pairPath = createDir(request, ROOT_DIRECTORY_NAME);
            String imageName = UserUtils.getRandomString(9) + imageExtension;
            dir = pairPath.getFirst() + File.separator + imageName;
            Path copyLocation = Paths
                    .get(pairPath.getSecond().toString() + File.separator + imageName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    private Pair<String, String> createDir(HttpServletRequest request, String dir) throws IOException {
        String uploadDir = dir;
        String realpath = request.getServletContext().getRealPath(dir);
        Path parent = Paths.get(realpath);
        if (!Files.exists(parent)) {
            Files.createDirectory(parent);
        }
        Path path;
        for (int i = 0; i < NUMBER_SUB_FOLDERS; i++) {
            String randomString = UserUtils.getRandomString(2);
            uploadDir = uploadDir + File.separator + randomString;
            path = Paths.get(realpath + File.separator + randomString);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            realpath = path.toString();
        }
        return Pair.of(uploadDir, realpath);
    }

    private String getImageExtension(String imageType) {
        if (imageType.equals("image/jpeg") || imageType.equals("image/jpg")) {
            return ".jpg";
        } else if (imageType.equals("image/png")) {
            return ".png";
        } else {
            throw new ImageException("Изображение должно быть png или jpg");
        }
    }
}
