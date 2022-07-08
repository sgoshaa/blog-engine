package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.ProfileRequestDto;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiProfileController {

    private final ProfileService profileService;

    public ApiProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("profile/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response>editingMyProfile(@RequestBody ProfileRequestDto profileRequestDto){
        return ResponseEntity.ok(profileService.editingMyProfile(profileRequestDto));
    }
}
