package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.PostLikeOrDisLikeRequest;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.PostVotes;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.PostVotesRepository;
import edu.spirinigor.blogengine.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostVotesService {

    private final PostVotesRepository postVotesRepository;
    private final PostService postService;

    @Transactional
    public Response putLike(PostLikeOrDisLikeRequest postLikeOrDisLikeRequest) {
        return putLikeOrDisLike(postLikeOrDisLikeRequest, (short) 1);
    }

    @Transactional
    public Response putDisLike(PostLikeOrDisLikeRequest postLikeOrDisLikeRequest) {
        return putLikeOrDisLike(postLikeOrDisLikeRequest, (short) -1);
    }

    private Response putLikeOrDisLike(PostLikeOrDisLikeRequest postLikeOrDisLikeRequest, short value) {
        User currentUser = UserUtils.getCurrentUser();
        Optional<PostVotes> byPostIdAndUserId = postVotesRepository.findByPostIdAndUserId(postLikeOrDisLikeRequest.getPostId()
                , currentUser.getId());
        Post postById = postService.getPostById(postLikeOrDisLikeRequest.getPostId());
        Response response = new Response();
        response.setResult(true);
        if (byPostIdAndUserId.isEmpty()) {
            PostVotes postVotes = new PostVotes();
            postVotes.setPost(postById);
            postVotes.setUser(currentUser);
            postVotes.setValue(value);
            postVotes.setTime(LocalDateTime.now());
            postVotesRepository.save(postVotes);
        } else if (byPostIdAndUserId.get().getValue() == - value) {
            PostVotes currentPostVotes = byPostIdAndUserId.get();
            currentPostVotes.setValue(value);
            postVotesRepository.save(currentPostVotes);
        } else {
            response.setResult(false);
            return response;
        }
        return response;
    }
}
