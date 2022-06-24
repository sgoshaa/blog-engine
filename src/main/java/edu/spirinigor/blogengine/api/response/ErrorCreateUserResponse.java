package edu.spirinigor.blogengine.api.response;

import edu.spirinigor.blogengine.dto.ErrorsCreatingUserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorCreateUserResponse {
    private Boolean result;
    private ErrorsCreatingUserDto errorsCreatingUserDto;
}
