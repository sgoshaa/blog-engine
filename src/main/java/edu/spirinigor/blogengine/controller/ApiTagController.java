package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.response.TagResponse;
import edu.spirinigor.blogengine.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tag")
public class ApiTagController {

    private final TagService tagService;

    public ApiTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public ResponseEntity<TagResponse> getListTag(
            @RequestParam(value = "query", defaultValue = "") String name){
        return new ResponseEntity<>(tagService.getListTag(), HttpStatus.OK);
    }
}
