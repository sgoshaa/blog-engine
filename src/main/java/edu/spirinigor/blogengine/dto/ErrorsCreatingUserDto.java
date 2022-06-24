package edu.spirinigor.blogengine.dto;
//{
//        "result": false,
//        "errors": {
//        "email": "Этот e-mail уже зарегистрирован",
//        "name": "Имя указано неверно",
//        "password": "Пароль короче 6-ти символов",
//        "captcha": "Код с картинки введён неверно"
//        }
//        }

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
