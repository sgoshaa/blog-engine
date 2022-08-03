package edu.spirinigor.blogengine.util;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageUtils {

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth
                , targetHeight, Scalr.OP_ANTIALIAS);
    }
}

