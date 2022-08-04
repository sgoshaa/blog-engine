package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiImageController {

    private final ImageService imageService;

    public ApiImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "api/image",
            consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile image, HttpServletRequest request) {
        return ResponseEntity.ok(imageService.uploadImage(image));
    }
}
