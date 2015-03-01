package jp.ne.glory.web.framework.filter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import jp.ne.glory.infra.certify.CertifySession;
import jp.ne.glory.infra.certify.CertifyTarget;
import org.glassfish.jersey.server.model.AnnotatedMethod;

/**
 * セッションチェックの機能を登録する.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class SessionSecurityFeature implements DynamicFeature {

    /**
     * セッション情報
     */
    @Inject
    private CertifySession certifySession;

    /**
     * セッションチェックのフィルターを登録する
     *
     * @param resourceInfo リソース情報
     * @param context コンテキスト
     */
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {

        AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());

        if (am.isAnnotationPresent(CertifyTarget.class)) {

            context.register(new SessionSecurityFilter(certifySession));
        }

        if (resourceInfo.getResourceClass().getAnnotation(CertifyTarget.class) != null) {

            context.register(new SessionSecurityFilter(certifySession));
        }
    }

}
