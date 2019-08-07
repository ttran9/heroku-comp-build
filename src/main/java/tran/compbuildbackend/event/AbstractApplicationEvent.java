package tran.compbuildbackend.event;

import org.springframework.context.ApplicationEvent;
import tran.compbuildbackend.domain.user.ApplicationUser;

import java.util.Locale;

public abstract class AbstractApplicationEvent extends ApplicationEvent {
    protected static final long serialVersionUID = 1L;
    protected String appUrl;
    protected Locale locale;
    protected ApplicationUser user;

    AbstractApplicationEvent(ApplicationUser user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }
}
