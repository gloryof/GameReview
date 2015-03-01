
package jp.ne.glory.web.framework.exception.mapper;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.infra.certify.CertifyException;
import jp.ne.glory.web.login.Login;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CertifyExceptionMapperTest {

    private CertifyExceptionMapper sut = null;

    @Before
    public void setUp() {

        sut = new CertifyExceptionMapper();
    }

    @Test
    public void ログイン画面へフォワードされる() {

        final Response actualResponse = sut.toResponse(new CertifyException());

        final URI uri = UriBuilder.fromResource(Login.class).build();

        assertThat(actualResponse.getStatus(), is(Response.Status.SEE_OTHER.getStatusCode()));
        assertThat(actualResponse.getLocation(), is(uri));

    }
}