package edu.spirinigor.blogengine.mapper.converter;

import edu.spirinigor.blogengine.model.Tag;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class TagConverter {
    @Named("tagNameToString")
    public String tagNameToString(Tag tag){
        return tag.getName();
    }
}
