package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha_codes")
@Data
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String code;

    @Column(name = "secret_code",nullable = false)
    private String secretCode;
}
