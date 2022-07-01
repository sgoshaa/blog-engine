package edu.spirinigor.blogengine.api.request;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private Long timestamp;
    private Short active;
    private String title;
    private List<String> tags;
    private String text;
}
