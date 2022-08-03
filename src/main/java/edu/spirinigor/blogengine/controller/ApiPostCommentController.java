package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.PostCommentRequest;
import edu.spirinigor.blogengine.api.response.PostCommentResponse;
import edu.spirinigor.blogengine.service.PostCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiPostCommentController {

    private final PostCommentService commentService;

    public ApiPostCommentController(PostCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostCommentResponse> createComment(@RequestBody PostCommentRequest request){
        return ResponseEntity.ok(commentService.createComment(request));
    }
}
