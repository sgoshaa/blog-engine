package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "isModerator", expression = "java((short)0)")
    @Mapping(target = "regTime", expression = "java(getDate())")
    User dtoToUser(CreateUserRequest userDto);

    default LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}

