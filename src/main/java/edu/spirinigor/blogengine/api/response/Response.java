package edu.spirinigor.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.spirinigor.blogengine.dto.ErrorsCreatingPostDto;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Boolean result;
    private Object errors;
}
