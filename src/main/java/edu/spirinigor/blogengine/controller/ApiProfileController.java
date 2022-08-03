package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.ProfileRequestDto;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/")
public class ApiProfileController {

    private final ProfileService profileService;

    public ApiProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> editingMyProfile(HttpServletRequest request, ProfileRequestDto profileRequestDto) {
        return ResponseEntity.ok(profileService.editingMyProfile(profileRequestDto, request));
    }

    @PostMapping(value = "profile/my", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> editingMyProfileJson(@RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(profileService.editingMyProfile(profileRequestDto, request));
    }
}
