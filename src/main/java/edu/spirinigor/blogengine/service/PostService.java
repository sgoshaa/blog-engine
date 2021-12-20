package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PostService {

    public PostResponse getListPost(){

        PostDTO postDTO = new PostDTO();
        postDTO.setId(345);
        postDTO.setTimeStamp(new Date().getTime());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(88);
        userDTO.setName("Дмитрий Петров");

        postDTO.setUser(userDTO);
        postDTO.setTitle("Заголовок поста");
        postDTO.setAnnounce("Текст анонса поста без HTML-тэгов");
        postDTO.setLikeCount(36);
        postDTO.setDislikeCount(3);
        postDTO.setCommentCount(15);
        postDTO.setViewCount(55);

        List<PostDTO>posts = new ArrayList<>();
        posts.add(postDTO);

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(1);
        postResponse.setPosts(posts);

        return postResponse;
    }
}
