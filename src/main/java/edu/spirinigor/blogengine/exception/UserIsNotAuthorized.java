package edu.spirinigor.blogengine.exception;

public class UserIsNotAuthorized extends RuntimeException {

    public UserIsNotAuthorized(String message) {
        super(message);
    }

}
