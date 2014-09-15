package jp.ne.groly.web.top.bean;

import java.time.LocalDateTime;

public class TopInfo {
    public final LocalDateTime now;

    public final String lastName;

    public final String firstName;

    public TopInfo(final LocalDateTime now, final String lastName, final String firstName) {
        this.now = now;
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
