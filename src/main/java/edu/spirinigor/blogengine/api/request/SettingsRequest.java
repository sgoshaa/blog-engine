package edu.spirinigor.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsRequest {
    @JsonProperty("MULTIUSER_MODE")
    private Boolean multiUserMode;
    @JsonProperty("POST_PREMODERATION")
    private Boolean postPreModeration;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private Boolean statisticsIsPublic;
}
