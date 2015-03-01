package jp.ne.glory.web.framework.exception.mapper;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import jp.ne.glory.infra.certify.CertifyException;
import jp.ne.glory.web.login.Login;

/**
 * 認証チェック例外のMapper. <br>
 * CertifyExceptionをキャッチし、ログイン画面に遷移させる
 *
 * @author Junki Yamada
 */
@Provider
public class CertifyExceptionMapper implements ExceptionMapper<CertifyException> {

    /**
     * レスポンスを返却する.
     *
     * @param exception 認証例外
     * @return ログイン画面へリダイレクト
     */
    @Override
    public Response toResponse(final CertifyException exception) {

        URI uri = UriBuilder.fromResource(Login.class).build();
        return Response.seeOther(uri).build();
    }

}
