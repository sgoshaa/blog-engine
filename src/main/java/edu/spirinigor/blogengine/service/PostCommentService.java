package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.PostCommentRequest;
import edu.spirinigor.blogengine.api.response.PostCommentResponse;
import edu.spirinigor.blogengine.exception.AnyException;
import edu.spirinigor.blogengine.mapper.CommentMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.PostComment;
import edu.spirinigor.blogengine.repository.PostCommentRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public PostCommentService(PostCommentRepository postCommentRepository, PostService postService) {
        this.postCommentRepository = postCommentRepository;
        this.postService = postService;
    }

    public PostCommentResponse createComment(PostCommentRequest request) {
        if (request.getText().isEmpty() || request.getText().length() < 3) {
            throw new AnyException("Текст слишком короткий, меньше 3 символов.");
        }
        Post post = postService.getPostById(request.getPostId());
        PostComment postComment = null;
        if (request.getParentId() != null) {
            postComment = getPostCommentById(request.getParentId());
        }
        PostComment s = commentMapper.toPostComment(request, postComment, post);
        PostComment save = postCommentRepository.save(s);

        PostCommentResponse postCommentResponse = new PostCommentResponse();
        postCommentResponse.setId(save.getId());
        return postCommentResponse;
    }

    private PostComment getPostCommentById(int id) {
        return postCommentRepository.findById(id).orElseThrow(
                () -> {
                    throw new AnyException("Комментарий с таким id = " + id + " не существует.");
                }
        );
    }
}
