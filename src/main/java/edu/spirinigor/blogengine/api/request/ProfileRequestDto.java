package edu.spirinigor.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileRequestDto {
    private String name;
    private String email;
    private String password;
}
