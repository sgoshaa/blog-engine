package edu.spirinigor.blogengine.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticResponse {
    private Integer postsCount;
    private Integer likesCount;
    private Integer dislikesCount;
    private Integer viewsCount;
    private Long firstPublication;
}
