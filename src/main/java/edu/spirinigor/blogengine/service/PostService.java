package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.CalendarResponse;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.specification.SearchPostSpecification;
import edu.spirinigor.blogengine.util.Pagination;
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

    public PostResponse getListPost(Integer offset, Integer limit, String mode) {
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
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postList.getTotalElements());
        postResponse.setPosts(postMapper.postToListDto(postList));
        return postResponse;
    }

    public PostResponse searchPost(Integer offset, Integer limit, String query) {
        if (query.isEmpty()) {
            return getListPost(offset, limit, "recent");
        }
        Page<Post> all =
                postRepository.findAll(postSpecification.getSpecification(query), pagination.getPage(offset, limit));
        if (all.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }
        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(postMapper.postToListDto(all));
        postResponse.setCount(all.getTotalElements());
        return postResponse;
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

    public PostResponse getPostByDate(Integer offset, Integer limit, Date date) {
        Page<Post> postByDate = postRepository.getPostByDate(date, pagination.getPage(offset, limit));
        if (postByDate.getTotalElements() == 0) {
            return getEmptyPostResponse();
        }
        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(postMapper.postToListDto(postByDate));
        postResponse.setCount(postByDate.getTotalElements());
        return postResponse;
    }

    private PostResponse getEmptyPostResponse() {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(0L);
        postResponse.setPosts(new ArrayList<>());
        return postResponse;
    }

    public PostResponse getPostByTag(Integer offset, Integer limit, String tag) {

        return new PostResponse();
    }
}
