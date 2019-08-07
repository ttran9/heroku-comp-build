package tran.compbuildbackend.event;

import tran.compbuildbackend.domain.user.ApplicationUser;

import java.util.Locale;

public class OnPasswordResetRequestEvent extends AbstractApplicationEvent {
    public OnPasswordResetRequestEvent(ApplicationUser user, Locale locale, String appUrl) {
        super(user, locale, appUrl);
    }
}
