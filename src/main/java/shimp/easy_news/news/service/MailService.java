package shimp.easy_news.news.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import shimp.easy_news.news.dto.MailRequest;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    private static final String senderEmail = "shiiiiimp@gmail.com";
    private final JavaMailSender javaMailSender;

    private MimeMessage createMail(String mail, String summary) {

        // Thymeleaf를 이용해 이메일 템플릿을 렌더링
        Context context = new Context();
        context.setVariable("summary", summary);

        String emailContent = templateEngine.process("mail", context);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(mail);
            helper.setSubject("메일 전송");
            helper.setText(emailContent, true);

            return message;
        } catch (MessagingException e) {
            log.error("이메일 생성 중 오류 발생", e);
            throw new RuntimeException("이메일 생성 실패");
        }
    }


    public void sendEmail(String email, String summary) {

//        User findUser = userRepository.findByEmail(email);
        log.info("여기 이메일 email: {}", email);
//        if (findUser == null) {
//            throw new IllegalArgumentException("실패");
//        }

        MimeMessage message = createMail(email, summary);
        javaMailSender.send(message);
    }
}
