package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public NoAuthCheckResponse authCheck(){
        NoAuthCheckResponse noAuthCheckResponse = new NoAuthCheckResponse();
        noAuthCheckResponse.setResult(false);
        return noAuthCheckResponse;
    }
}
