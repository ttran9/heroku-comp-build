package tran.compbuildbackend.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tran.compbuildbackend.event.utility.EventUtil;
import tran.compbuildbackend.services.verificationtoken.EmailVerificationTokenServiceImpl;

import static tran.compbuildbackend.constants.email.EmailConstants.REGISTRATION_SUCCESS_CONFIRMATION;
import static tran.compbuildbackend.constants.mapping.MappingConstants.CONFIRM_REGISTRATION_URL;

@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {
    @Autowired
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onApplicationEvent(OnRegistrationSuccessEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationSuccessEvent event) {
        try {
            String subject = "Confirm Registration";
            EventUtil.sendEmail(event, emailVerificationTokenService, messageSource, REGISTRATION_SUCCESS_CONFIRMATION,
                    mailSender, subject, CONFIRM_REGISTRATION_URL);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new UsernameNotFoundException("account cannot be created at this time");
        }
    }

}
