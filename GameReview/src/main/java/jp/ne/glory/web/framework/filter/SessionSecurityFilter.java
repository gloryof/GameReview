package jp.ne.glory.web.framework.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import jp.ne.glory.infra.certify.CertifyException;
import jp.ne.glory.infra.certify.CertifySession;
import jp.ne.glory.infra.certify.CertifyTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * セッションのチェックを行う.
 *
 * @author Junki Yamada
 */
@Priority(Priorities.AUTHENTICATION)
@CertifyTarget
public class SessionSecurityFilter implements ContainerRequestFilter {

    /**
     * ロガー.
     */
//    private static final Logger logger = LoggerFactory.getLogger(SessionSecurityFilter.class);

    /**
     * セッション認証情報.
     */
    private final CertifySession certify;

    public SessionSecurityFilter(final CertifySession certify) {

        this.certify = certify;
    }

    /**
     * ログインチェック処理. 
     *
     * @param requestContext リクエストコンテキスト
     * @throws IOException
     * @throws CertifyException ログインしていない場合にスローされる
     */
    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {

        final boolean isCertifyFail = (certify == null) || (!certify.isActive());

        if (isCertifyFail) {

            writeFailedError(requestContext);
            throw new CertifyException();
        }

    }

    /**
     * 失敗時のエラーログを出力する.
     *
     * @param requestContext リクエストコンテキスト
     */
    private void writeFailedError(final ContainerRequestContext requestContext) {

        final StringBuilder builder = new StringBuilder();

        builder.append("[CertifyError]");
        builder.append(" path='");
        builder.append(requestContext.getUriInfo().getPath());
        builder.append("'");

        Logger logger = LoggerFactory.getLogger(SessionSecurityFilter.class);
        logger.info(builder.toString());
    }
}
