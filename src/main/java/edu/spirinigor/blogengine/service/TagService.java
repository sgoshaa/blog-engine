package edu.spirinigor.blogengine.service;


import edu.spirinigor.blogengine.api.response.TagResponse;
import edu.spirinigor.blogengine.dto.TagDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    public TagResponse getListTag() {

        List<TagDTO> list = new ArrayList<>();
        list.add(new TagDTO("Java",1.0));
        list.add(new TagDTO("Spring",0.56));
        list.add(new TagDTO("Hibernate",0.22));
        list.add(new TagDTO("Hadoop",0.17));

        return new TagResponse(list);

    }
}

