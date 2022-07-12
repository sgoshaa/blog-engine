package edu.spirinigor.blogengine.exception;

import edu.spirinigor.blogengine.dto.ProfileErrorsDto;

public class ErrorsProfileException extends RuntimeException {

    private ProfileErrorsDto profileErrorsDto;

    public ErrorsProfileException(String message, ProfileErrorsDto profileErrorsDto) {
        super(message);
        this.profileErrorsDto = profileErrorsDto;
    }
}
