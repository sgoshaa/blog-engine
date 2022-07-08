package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.CreatePostRequest;
import edu.spirinigor.blogengine.api.request.ModerationRequest;
import edu.spirinigor.blogengine.api.response.OperationsOnPostResponse;
import edu.spirinigor.blogengine.api.response.CalendarResponse;
import edu.spirinigor.blogengine.api.response.ListPostResponse;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.ErrorsCreatingPostDto;
import edu.spirinigor.blogengine.exception.AnyException;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.Tag;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.repository.specification.SearchPostSpecification;
import edu.spirinigor.blogengine.util.Pagination;
import edu.spirinigor.blogengine.util.UserUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final Pagination pagination;
    private final SearchPostSpecification postSpecification;
    private final UserRepository userRepository;
    private final TagService tagService;

    public PostService(PostRepository postRepository, Pagination pagination, SearchPostSpecification postSpecification,
                       UserRepository userRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.pagination = pagination;
        this.postSpecification = postSpecification;
        this.userRepository = userRepository;
        this.tagService = tagService;
    }

    public ListPostResponse getListPost(Integer offset, Integer limit, String mode) {
        Page<Post> postList;
        switch (mode) {
            case "popular":
                postList = postRepository.findPopularPost(pagination.getPage(offset, limit));
                break;
            case "best":
                postList = postRepository.findBestPost(pagination.getPage(offset, limit));
                break;
            case "early"://early - сортировать по дате публикации, выводить сначала старые
                postList = postRepository.findEarlyPost(pagination.getPage(offset, limit));
                break;
            default://recent - сортировать по дате публикации, выводить сначала новые (если mode не задан
                // , использовать это значение по умолчанию)
                postList = postRepository.findRecentPost(pagination.getPage(offset, limit));
                break;
        }
        return getListPostResponse(postList);
    }

    public ListPostResponse searchPost(Integer offset, Integer limit, String query) {
        if (query.isEmpty()) {
            return getListPost(offset, limit, "recent");
        }
        Page<Post> all =
                postRepository.findAll(postSpecification.getSpecification(query), pagination.getPage(offset, limit));
        if (all.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }
        return getListPostResponse(all);
    }

    public CalendarResponse getCalendar(Integer year) {
        if (year == 0) {
            year = LocalDateTime.now().getYear();
        }
        List<Post> result = postRepository.getCalendarByYear(year);
        Map<LocalDate, List<Post>> collect = result.stream().collect(
                Collectors.groupingBy(post -> post.getTime().toLocalDate())
        );
        Set<Map.Entry<LocalDate, List<Post>>> entries = collect.entrySet();
        HashMap<LocalDate, Integer> hashMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<Post>> entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue().size());
        }
        List<Integer> years = postRepository.getCalendar();
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setYears(years);
        calendarResponse.setPosts(hashMap);
        return calendarResponse;
    }

    public ListPostResponse getPostByDate(Integer offset, Integer limit, Date date) {
        Page<Post> postByDate = postRepository.getPostByDate(date, pagination.getPage(offset, limit));
        if (postByDate.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }
        return getListPostResponse(postByDate);
    }

    public ListPostResponse getPostByTag(Integer offset, Integer limit, String tag) {
        Page<Post> postByTagName = postRepository.getPostByTagName(tag, pagination.getPage(offset, limit));
        if (postByTagName.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }
        return getListPostResponse(postByTagName);
    }

    public PostResponse getPost(Integer id) {
        Post byId = postRepository.getPostById(id, LocalDateTime.now());
        if (byId == null) {
            return null;
        }
        PostResponse postResponse = postMapper.postToPostResponse(byId);
        updateViewCount(byId);
        return postResponse;
    }

    public ListPostResponse getMyPost(Integer offset, Integer limit, String status) {
        Integer idCurrentUser = UserUtils.getIdCurrentUser();
        Page<Post> allMyPost = null;
        switch (status) {
            case "inactive":
                allMyPost = postRepository.findAllMyByStatusInActive(idCurrentUser, pagination.getPage(offset, limit));
                break;
            case "pending":
                allMyPost = postRepository.findAllMyByStatus(idCurrentUser
                        , ModerationStatus.NEW, pagination.getPage(offset, limit));
                break;
            case "declined":
                allMyPost = postRepository.findAllMyByStatus(idCurrentUser
                        , ModerationStatus.DECLINED, pagination.getPage(offset, limit));
                break;
            case "published":
                allMyPost = postRepository.findAllMyByStatus(idCurrentUser
                        , ModerationStatus.ACCEPTED, pagination.getPage(offset, limit));
                break;
        }

        if (allMyPost.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }

        return getListPostResponse(allMyPost);
    }

    public ListPostResponse getPostForModeration(Integer offset, Integer limit, String status) {
        Integer idCurrentUser = UserUtils.getIdCurrentUser();
        Page<Post> allPost = null;
        switch (status) {
            case "new":
                allPost = postRepository.findAllForModeration(
                        ModerationStatus.NEW, pagination.getPage(offset, limit, Sort.by("time").descending())
                );
                break;
            case "declined":
                allPost = postRepository.findAllForModerationMy(idCurrentUser
                        , ModerationStatus.DECLINED, pagination.getPage(offset, limit, Sort.by("time").descending()));
                break;
            case "accepted":
                allPost = postRepository.findAllForModerationMy(idCurrentUser
                        , ModerationStatus.ACCEPTED, pagination.getPage(offset, limit, Sort.by("time").descending()));
                break;
        }

        assert allPost != null;

        if (allPost.isEmpty()) {
            return getEmptyPostResponse();
        }
        return getListPostResponse(allPost);
    }

    public OperationsOnPostResponse addPost(CreatePostRequest createPostRequest) {
        OperationsOnPostResponse operationsOnPostResponse = new OperationsOnPostResponse();
        ErrorsCreatingPostDto errorsCreatingPostDto = errorCheckCreatePostRequest(createPostRequest);
        if (errorsCreatingPostDto != null) {
            operationsOnPostResponse.setResult(false);
            operationsOnPostResponse.setErrorsCreatingPostDto(errorsCreatingPostDto);
            return operationsOnPostResponse;
        }
        Post post = postMapper.toPost(createPostRequest);
        List<Tag> tags = tagService.getExistingTagsOrCreateNew(createPostRequest.getTags());
        post.setTags(tags);
        User currentUser = userRepository.findById(UserUtils.getIdCurrentUser()).get();
        post.setUser(currentUser);
        postRepository.save(post);

        operationsOnPostResponse.setResult(true);
        return operationsOnPostResponse;
    }

    @Transactional
    public OperationsOnPostResponse updatePost(Integer id, CreatePostRequest request) {
        Post currentPost = getPostById(id);
        Post updatedPost = postMapper.toPost(request);
        Post post = postMapper.updatePost(currentPost, updatedPost);
        if (currentPost.getUser().equals(UserUtils.getCurrentUser())) {
            post.setModerationStatus(ModerationStatus.NEW);
        }
        post.setTags(tagService.getExistingTagsOrCreateNew(request.getTags()));
        postRepository.save(post);
        OperationsOnPostResponse operationsOnPostResponse = new OperationsOnPostResponse();
        operationsOnPostResponse.setResult(true);
        return operationsOnPostResponse;
    }

    private void updateViewCount(Post byId) {
        Integer idCurrentUser = UserUtils.getIdCurrentUser();
        if (byId.getModerator().getId() == idCurrentUser || byId.getUser().getId() == idCurrentUser) {
            return;
        }
        byId.setViewCount(byId.getViewCount() + 1);
        postRepository.save(byId);
    }

    private ListPostResponse getListPostResponse(Page<Post> posts) {
        ListPostResponse listPostResponse = new ListPostResponse();
        listPostResponse.setCount(posts.getTotalElements());
        listPostResponse.setPosts(postMapper.postToListDto(posts));
        return listPostResponse;
    }

    private ListPostResponse getEmptyPostResponse() {
        ListPostResponse listPostResponse = new ListPostResponse();
        listPostResponse.setCount(0L);
        listPostResponse.setPosts(new ArrayList<>());
        return listPostResponse;
    }

    private ErrorsCreatingPostDto errorCheckCreatePostRequest(CreatePostRequest createPostRequest) {
        ErrorsCreatingPostDto errorsCreatingPostDto = new ErrorsCreatingPostDto();
        if (createPostRequest.getTitle().length() <= 3) {
            errorsCreatingPostDto.setTitleField();
        }
        if (createPostRequest.getText().length() <= 50) {
            errorsCreatingPostDto.setTextField();
        }
        if (errorsCreatingPostDto.getText().isEmpty() && errorsCreatingPostDto.getTitle().isEmpty()) {
            errorsCreatingPostDto = null;
        }
        return errorsCreatingPostDto;
    }

    public OperationsOnPostResponse moderationPost(ModerationRequest request) {
        OperationsOnPostResponse operationsOnPostResponse = new OperationsOnPostResponse();
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser.getIsModerator() != 1) {
            operationsOnPostResponse.setResult(false);
            return operationsOnPostResponse;
        }
        Post post = getPostById(request.getPostId());

        switch (request.getDecision()) {
            case "accept":
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                break;
            case "decline":
                post.setModerationStatus(ModerationStatus.DECLINED);
                break;
        }
        post.setModerator(currentUser);
        postRepository.save(post);

        operationsOnPostResponse.setResult(true);
        return operationsOnPostResponse;
    }

    public Post getPostById(int id) {
        return postRepository.findById(id).orElseThrow(
                () -> new AnyException("Пост с таким id = " + id + " не существует."));
    }
}
