package edu.spirinigor.blogengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsCreatingUserDto {
    private String email;
    private String name;
    private String password;
    private String captcha;
}
