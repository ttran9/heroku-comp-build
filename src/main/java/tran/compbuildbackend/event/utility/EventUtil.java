package tran.compbuildbackend.event.utility;

import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.event.AbstractApplicationEvent;
import tran.compbuildbackend.services.verificationtoken.VerificationTokenService;

public class EventUtil {

    public static void sendEmail(AbstractApplicationEvent event, VerificationTokenService tokenService,
                                 MessageSource messageSource, String messsageContent,
                                 JavaMailSender mailSender, String subject, String urlPath) {
        ApplicationUser user = event.getUser();
        String token = tokenService.createVerificationToken(user);

        String recipient = user.getEmail();

        String url = event.getAppUrl() + urlPath + token;
        String emailContent = messageSource.getMessage(messsageContent, null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(emailContent + "\n" + url);
        mailSender.send(email);

    }
}
