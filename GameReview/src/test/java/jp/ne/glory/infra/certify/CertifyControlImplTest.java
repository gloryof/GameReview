
package jp.ne.glory.infra.certify;

import jp.ne.glory.application.user.UserSearch;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class CertifyControlImplTest {

    public static class ユーザが見つかった場合 {

        private CertifyControlImpl sut = null;
        private UserRepositoryStub stub = null;

        private UserName excpectedUserName = null;
        private UserId expectedUserId = null;
        private final LoginId paramLoginId = new LoginId("tes-user");

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            excpectedUserName = new UserName("テストユーザ");
            expectedUserId = new UserId(100L);

            final User user = new User(expectedUserId);
            user.setUserName(excpectedUserName);
            user.setLoginId(paramLoginId);

            stub.save(user);

            sut = new CertifyControlImpl(new CertifySession(), new UserSearch(stub));
        }

        @Test
        public void 認証情報に値が設定される() {

            final CertifySession actualCertify = sut.createAuthentication(paramLoginId);

            assertThat(actualCertify.isActive(), is(true));
            assertThat(actualCertify.getUserId().getValue(), is(expectedUserId.getValue()));
            assertThat(actualCertify.getUserName().getValue(), is(excpectedUserName.getValue()));

        }
    }
    public static class ユーザが見つからない場合 {

        private CertifyControlImpl sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();
            sut = new CertifyControlImpl(new CertifySession(), new UserSearch(stub));
        }

        @Test
        public void 認証情報に値が未設定状態になる() {

            final CertifySession actualCertify = sut.createAuthentication(new LoginId("test-user"));

            assertThat(actualCertify.isActive(), is(false));
            assertThat(actualCertify.getUserId().getValue(), is(UserId.notNumberingValue().getValue()));
            assertThat(actualCertify.getUserName().getValue(), is(UserName.empty().getValue()));

        }
    }

}