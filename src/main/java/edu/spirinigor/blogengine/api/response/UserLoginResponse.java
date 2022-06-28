package edu.spirinigor.blogengine.api.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private Integer id;
    private String name;
    private String photo;
    private String email;
    private Boolean moderation;
    private Integer moderationCount;
    private Boolean settings;
}
