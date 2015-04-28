package jp.ne.glory.domain.user.validate;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.common.error.ErrorInfo;
import jp.ne.glory.domain.common.error.ValidateError;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepositoryStub;
import jp.ne.glory.domain.user.value.Authorities;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.test.validate.ValidateErrorsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UserModifyCommonValidateRuleTest {

    public static class 全ての値が正常に設定されている場合 {

        private UserModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            final User user = new User(new UserId(1L));

            user.getAuthorities().add(Authority.ReviewPost);
            user.setLoginId(new LoginId("test"));
            user.setUserName(new UserName("テストユーザ"));
            user.setPassword(new Password("19CB2A070DDBE8157E17C5DDA0EA38E8AA16FAE1725C1F7AC22747D870368579"));

            sut = new UserModifyCommonValidateRule(user, new UserRepositoryStub());
        }

        @Test
        public void validateを実行しても入力チェックエラーにならない() {

            final ValidateErrors actualErrors = sut.validate();

            assertThat(actualErrors.hasError(), is(false));
        }
    }

    public static class 全ての項目が未設定の場合 {

        private UserModifyCommonValidateRule sut = null;

        @Before
        public void setUp() {

            sut = new UserModifyCommonValidateRule(new User(), new UserRepositoryStub());
        }

        @Test
        public void validateで必須項目がエラーチェックになる() {

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));

            final List<ValidateError> errorList = new ArrayList<>();

            errorList.add(new ValidateError(ErrorInfo.Required, UserName.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, LoginId.LABEL));
            errorList.add(new ValidateError(ErrorInfo.RequiredSelectOne, Authorities.LABEL));
            errorList.add(new ValidateError(ErrorInfo.Required, Password.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }
    }

    public static class 既に登録されているログインIDが設定されている場合 {

        private UserModifyCommonValidateRule sut = null;
        private UserRepositoryStub stub = null;

        @Before
        public void setUp() {

            stub = new UserRepositoryStub();

            final User savedUser = new User(new UserId(1l));
            savedUser.setLoginId(new LoginId("login-user"));
            savedUser.setUserName(new UserName("ログインユーザ"));
            savedUser.setPassword(new Password("password"));
            savedUser.getAuthorities().add(Authority.None);

            stub.save(savedUser);
        }

        @Test
        public void 別ユーザを同一ログインIDはエラー() {

            final User newUser = new User(new UserId(2l));
            final User savedUser = stub.findAll().get(0);

            newUser.setLoginId(savedUser.getLoginId());
            newUser.setUserName(new UserName("ログインユーザ2"));
            newUser.setPassword(new Password("password2"));
            newUser.getAuthorities().add(Authority.None);

            sut = new UserModifyCommonValidateRule(newUser, stub);

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(true));
            assertThat(actual.toList().size(), is(1));

            final List<ValidateError> errorList = new ArrayList<>();
            errorList.add(new ValidateError(ErrorInfo.LoginIdDuplicated, LoginId.LABEL));

            final ValidateErrorsHelper helper = new ValidateErrorsHelper(actual);
            helper.assertErrors(errorList);
        }

        @Test
        public void 同一ユーザを同一ログインIDはエラーにならない() {

            final User savedUser = stub.findAll().get(0);
            final User editUser = new User(savedUser.getUserId());

            editUser.setLoginId(savedUser.getLoginId());
            editUser.setUserName(new UserName("ログインユーザ2"));
            editUser.setPassword(new Password("password2"));
            editUser.getAuthorities().add(Authority.None);

            sut = new UserModifyCommonValidateRule(editUser, stub);

            final ValidateErrors actual = sut.validate();

            assertThat(actual.hasError(), is(false));
        }
    }
}
