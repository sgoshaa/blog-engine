package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.StatisticResponse;
import edu.spirinigor.blogengine.exception.StatisticsNotPublicException;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.util.UserUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final PostRepository postRepository;
    private final DateConverter dateConverter;
    private final SettingService settingService;

    public StatisticService(PostRepository postRepository, DateConverter dateConverter, SettingService settingService) {
        this.postRepository = postRepository;
        this.dateConverter = dateConverter;
        this.settingService = settingService;
    }

    public StatisticResponse getMyStatistic() {
        List<Post> posts = postRepository.findAllByUserAndIsActive(UserUtils.getCurrentUser(), (short) 1);
        return getStatistics(posts);
    }

    public StatisticResponse getAllStatistic() {
        if (!settingService.isStatisticsIsPublic() && UserUtils.getCurrentUser().getIsModerator() == 0) {
            throw new StatisticsNotPublicException("Статистика не публична");
        }
        List<Post> all = postRepository.findAll();
        List<Post> collect = all.stream()
                .filter(post -> post.getIsActive() == (short) 1)
                .collect(Collectors.toList());
        return getStatistics(collect);
    }

    private StatisticResponse getStatistics(List<Post> posts) {
        StatisticResponse statisticResponse = new StatisticResponse();
        statisticResponse.setViewsCount(posts.stream().mapToInt(Post::getViewCount).sum());
        statisticResponse.setDislikesCount(getLikeOrDisLikeCount(posts, -1));
        statisticResponse.setLikesCount(getLikeOrDisLikeCount(posts, 1));
        statisticResponse.setPostsCount(posts.size());
        statisticResponse.setFirstPublication(
                dateConverter.convertDate(
                        posts.stream()
                                .map(Post::getTime)
                                .sorted()
                                .findFirst()
                                .get()
                )
        );
        return statisticResponse;
    }

    private int getLikeOrDisLikeCount(List<Post> posts, int x) {
        return (int) posts.stream()
                .flatMap(post -> post.getPostVotes()
                        .stream())
                .mapToInt(value -> value.getValue().intValue())
                .filter(value -> value == x)
                .count();
    }
}
