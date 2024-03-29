package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.CreatePostRequest;
import edu.spirinigor.blogengine.api.request.ModerationRequest;
import edu.spirinigor.blogengine.api.request.PostLikeOrDisLikeRequest;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.api.response.CalendarResponse;
import edu.spirinigor.blogengine.api.response.ListPostResponse;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.service.PostService;
import edu.spirinigor.blogengine.service.PostVotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping("/api/")
public class ApiPostController {

    private final PostService postService;
    private final PostVotesService postVotesService;

    public ApiPostController(PostService postService, PostVotesService postVotesService) {
        this.postService = postService;
        this.postVotesService = postVotesService;
    }

    @GetMapping("post")
    public ResponseEntity<ListPostResponse> getListPost(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "mode", defaultValue = "recent") String mode) {
        return new ResponseEntity<>(postService.getListPost(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("post/search")
    public ResponseEntity<ListPostResponse> searchPost(
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

    @GetMapping("post/byDate")
    public ResponseEntity<ListPostResponse> getPostByDate(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "date", defaultValue = "") String date) {
        return new ResponseEntity<>(postService.getPostByDate(offset, limit, Date.valueOf(date)), HttpStatus.OK);
    }

    @GetMapping("post/byTag")
    public ResponseEntity<ListPostResponse> getPostByTag(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "tag", defaultValue = "") String tag) {
        return new ResponseEntity<>(postService.getPostByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("post/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Integer id) {
        PostResponse postById = postService.getPost(id);
        if (postById == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postById, HttpStatus.OK);
    }

    @GetMapping("post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListPostResponse> getMyPost(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "status", defaultValue = "") String status) {
        return ResponseEntity.ok(postService.getMyPost(offset, limit, status));
    }

    @GetMapping("post/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ListPostResponse> getPostForModeration(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "status", defaultValue = "") String status) {
        return ResponseEntity.ok(postService.getPostForModeration(offset, limit, status));
    }

    @PostMapping("post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> addPost(@RequestBody CreatePostRequest createPostRequest) {
        return ResponseEntity.ok(postService.addPost(createPostRequest));
    }

    @PutMapping("post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> updatePost(@PathVariable Integer id,
                                               @RequestBody CreatePostRequest createPostRequest) {
        return ResponseEntity.ok(postService.updatePost(id, createPostRequest));
    }

    @PostMapping("moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<Response> moderationPost(@RequestBody ModerationRequest request) {
        return ResponseEntity.ok(postService.moderationPost(request));
    }

    @PostMapping("post/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> putLike(@RequestBody PostLikeOrDisLikeRequest postLikeOrDisLikeRequest){
        return ResponseEntity.ok(postVotesService.putLike(postLikeOrDisLikeRequest));
    }

    @PostMapping("post/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Response> putDisLike(@RequestBody PostLikeOrDisLikeRequest postLikeOrDisLikeRequest){
        return ResponseEntity.ok(postVotesService.putDisLike(postLikeOrDisLikeRequest));
    }

}
