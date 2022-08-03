package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.PasswordRecoveryRequest;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.repository.UserRepository;
import edu.spirinigor.blogengine.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailService {

    @Value("${blog.main-link}")
    private String MAIN_LINK;
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    @Transactional
    public Response sendRecoveryEmail(PasswordRecoveryRequest passwordRecoveryRequest) {
        Optional<User> byEmail = userRepository.findByEmail(passwordRecoveryRequest.getEmail());
        Response response = new Response();
        if (byEmail.isEmpty()) {
            response.setResult(false);
            return response;
        }
        User user = byEmail.get();
        String s = sendMessage(user.getEmail());
        user.setCode(s);
        userRepository.save(user);
        response.setResult(true);
        return response;
    }

    private String sendMessage(String to) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        String hash = UserUtils.getRandomString(25);
        String htmlMsg = createMessage(hash);
        try {
            messageHelper.setFrom("DevPub <sgoshaa@yandex.ru>");
            messageHelper.setTo(to);
            messageHelper.setSubject("Восстановление пароля для DevPub");
            messageHelper.setText(htmlMsg, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private String createMessage(String hash) {
        String link = MAIN_LINK + "/login/change-password/" + hash;
        return "<body>Перейдите по данной ссылки для восстановления пароля: " +
                "<a href=\"" + link + "\">Восстановление пароля</a></body>";
    }

}
