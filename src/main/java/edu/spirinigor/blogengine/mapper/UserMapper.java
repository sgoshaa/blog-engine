package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.api.response.UserLoginResponse;
import edu.spirinigor.blogengine.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "isModerator", expression = "java((short)0)")
    @Mapping(target = "regTime", expression = "java(getDate())")
    @Mapping(target = "code", source = "captcha")
    User dtoToUser(CreateUserRequest userDto);


    @Mapping(target = "moderationCount", expression = "java(0)")
    @Mapping(target = "moderation", expression = "java(user.getIsModerator() == 1)")
    @Mapping(target = "settings", expression = "java(user.getIsModerator() == 1)")
    UserLoginResponse toUserLoginResponse(User user);

    default LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}

