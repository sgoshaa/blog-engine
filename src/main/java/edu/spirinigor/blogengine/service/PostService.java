package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.CalendarResponse;
import edu.spirinigor.blogengine.api.response.ListPostResponse;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.specification.SearchPostSpecification;
import edu.spirinigor.blogengine.util.Pagination;
import edu.spirinigor.blogengine.util.UserUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public PostService(PostRepository postRepository, Pagination pagination, SearchPostSpecification postSpecification) {
        this.postRepository = postRepository;
        this.pagination = pagination;
        this.postSpecification = postSpecification;
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
                postList = postRepository.findAll(pagination.getPage(offset, limit, Sort.by("time").ascending()));
                break;
            default://recent - сортировать по дате публикации, выводить сначала новые (если mode не задан
                // , использовать это значение по умолчанию)
                postList = postRepository.findAll(pagination.getPage(offset, limit, Sort.by("time").descending()));
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

    public PostResponse getPostById(Integer id) {
        Post byId = postRepository.getPostById(id);
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
                allMyPost = postRepository.findAllMyByStatusInactive(idCurrentUser, pagination.getPage(offset, limit));
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
                allPost = postRepository.findAllForModeration(idCurrentUser
                        , ModerationStatus.NEW, pagination.getPage(offset, limit));
                break;
            case "declined":
                allPost = postRepository.findAllForModeration(idCurrentUser
                        , ModerationStatus.DECLINED, pagination.getPage(offset, limit));
                break;
            case "accepted":
                allPost = postRepository.findAllForModeration(idCurrentUser
                        , ModerationStatus.ACCEPTED, pagination.getPage(offset, limit));
                break;
        }

        assert allPost != null;

        if (allPost.isEmpty()){
            return getEmptyPostResponse();
        }
        return getListPostResponse(allPost);
    }

    private void updateViewCount(Post byId) {
        Integer idCurrentUser = UserUtils.getIdCurrentUser();
        if (byId.getModerator().getId() == idCurrentUser || byId.getUser().getId() == idCurrentUser ) {
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
}
