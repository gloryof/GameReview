package jp.ne.glory.web;

import javax.ws.rs.ApplicationPath;
import jp.ne.glory.web.framework.filter.SessionSecurityFeature;
import jp.ne.glory.web.framework.thymeleaf.ThymeleafViewProcessor;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.MvcFeature;

/**
 * アプリケーション設定
 *
 * @author Junki Yamada
 */
@ApplicationPath("/")
public class ApplicationSetting extends ResourceConfig {

    /**
     * コンストラクタ.<br>
     * アプリケーションの設定を行う。
     */
    public ApplicationSetting() {

        packages(this.getClass().getPackage().getName());

        register(ThymeleafViewProcessor.class);

        register(MvcFeature.class);
        register(SessionSecurityFeature.class);

        register(LoggingFilter.class);
    }
}
