package edu.spirinigor.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.spirinigor.blogengine.dto.ErrorsCreatingUserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponse {
    private Boolean result;
    @JsonProperty("errors")
    private ErrorsCreatingUserDto errorsCreatingUserDto;
}
