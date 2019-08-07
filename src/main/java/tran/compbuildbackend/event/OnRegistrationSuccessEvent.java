package tran.compbuildbackend.event;

import tran.compbuildbackend.domain.user.ApplicationUser;

import java.util.Locale;

public class OnRegistrationSuccessEvent extends AbstractApplicationEvent {

    public OnRegistrationSuccessEvent(ApplicationUser user, Locale locale, String appUrl) {
        super(user, locale, appUrl);
    }
}
