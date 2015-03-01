package jp.ne.glory.web.framework.filter;

import java.io.IOException;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.infra.certify.CertifyException;
import jp.ne.glory.infra.certify.CertifySession;
import jp.ne.glory.test.web.ContainerRequestContextStub;
import jp.ne.glory.test.web.UriInfoStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;


@RunWith(Enclosed.class)
public class SessionSecurityFilterTest {

    public static class ログインがされている場合 {

        private SessionSecurityFilter sut = null;
        @Before
        public void setUp() {

            final CertifySession session = new CertifySession();
            session.setActive(true);
            session.setUserId(new UserId(1l));
            session.setUserName(new UserName("テストユーザ"));

            sut = new SessionSecurityFilter(session);
        }

        @Test
        public void 例外がスローされない() throws IOException {

            sut.filter(new ContainerRequestContextStub());
        }
    }

    public static class ログインがされていない場合 {

        private SessionSecurityFilter sut = null;
        private ContainerRequestContextStub requestContext = null;

        @Before
        public void setUp() {

            final CertifySession session = new CertifySession();
            session.setActive(false);

            requestContext = new ContainerRequestContextStub();

            final UriInfoStub uriInfo = new UriInfoStub();
            uriInfo.setPath("/test/path");

            requestContext.setUriInfo(uriInfo);

            sut = new SessionSecurityFilter(session);

        }

        @Test(expected = CertifyException.class)
        public void 例外がスローされる() throws IOException {

            sut.filter(requestContext);
        }
    }

    public static class ログインがNullの場合 {

        private SessionSecurityFilter sut = null;
        private ContainerRequestContextStub requestContext = null;

        @Before
        public void setUp() {

            requestContext = new ContainerRequestContextStub();

            final UriInfoStub uriInfo = new UriInfoStub();
            uriInfo.setPath("/test/path");

            requestContext.setUriInfo(uriInfo);

            sut = new SessionSecurityFilter(null);
        }

        @Test(expected = CertifyException.class)
        public void 例外がスローされる() throws IOException {

            sut.filter(requestContext);
        }
    }
}
