package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.model.CaptchaCode;
import edu.spirinigor.blogengine.repository.CaptchaCodeRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class Scheduler {

    private final CaptchaCodeRepository captchaCodeRepository;

    public Scheduler(CaptchaCodeRepository captchaCodeRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deletingOutdatedCaptcha() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusHours(1);
        List<CaptchaCode> allOld = captchaCodeRepository.findAllOld(time);
        if (allOld.isEmpty()) {
            return;
        }
        captchaCodeRepository.deleteAll(allOld);
        log.log(Level.INFO, "Удалены устаревшие записи из таблицы Captcha_code c id : " + allOld.stream().
                map(CaptchaCode::getId).collect(Collectors.toList()));
    }
}
