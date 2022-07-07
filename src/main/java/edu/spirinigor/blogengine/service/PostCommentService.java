package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.PostCommentRequest;
import edu.spirinigor.blogengine.api.response.PostCommentResponse;
import edu.spirinigor.blogengine.mapper.CommentMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.PostComment;
import edu.spirinigor.blogengine.repository.PostCommentRepository;
import edu.spirinigor.blogengine.repository.PostRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public PostCommentService(PostCommentRepository postCommentRepository, PostRepository postRepository) {
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
    }


    public PostCommentResponse createComment(PostCommentRequest request) {

        Post post = postRepository.findById(request.getPostId()).orElseThrow(
                () -> {
                    throw new RuntimeException("Пост с таким id не существует");
                }
        );

        PostComment postComment = postCommentRepository.findById(request.getParentId()).orElseThrow(
                () -> {
                    throw new RuntimeException("Комментарий с таким id не существует");
                });

        PostComment s = commentMapper.toPostComment(request, postComment, post);
        PostComment save = postCommentRepository.save(s);

        PostCommentResponse postCommentResponse = new PostCommentResponse();
        postCommentResponse.setId(save.getId());
        return postCommentResponse;
    }
}
