package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.response.CalendarResponse;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("post")
    public ResponseEntity<PostResponse> getListPost(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "mode", defaultValue = "recent") String mode) {
        return new ResponseEntity<>(postService.getListPost(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("post/search")
    public ResponseEntity<PostResponse> searchPost(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "query", defaultValue = "") String query) {
        return new ResponseEntity<>(postService.searchPost(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("calendar")
    public ResponseEntity<CalendarResponse> getCalendar(
            @RequestParam(value = "year", defaultValue = "0") Integer year) {
        return new ResponseEntity<>(postService.getCalendar(year), HttpStatus.OK);
    }
}
