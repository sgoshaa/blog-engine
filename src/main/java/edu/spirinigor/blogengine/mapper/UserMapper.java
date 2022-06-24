package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User dtoToUser(CreateUserRequest userDto);
}
