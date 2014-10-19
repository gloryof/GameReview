/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.web;

import javax.ws.rs.ApplicationPath;
import jp.ne.glory.web.framework.ThymeleafViewProcessor;
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

        register(LoggingFilter.class);
    }
}
