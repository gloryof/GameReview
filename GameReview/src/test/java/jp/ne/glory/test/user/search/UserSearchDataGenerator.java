package jp.ne.glory.test.user.search;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;

public class UserSearchDataGenerator {

    public static List<User> creaeteUsers(final int count) {

        return IntStream.rangeClosed(0, count).asLongStream()
                .mapToObj(v -> createUser(v))
                .collect(Collectors.toList());
    }

    private static User createUser(final long userId) {

        final User user = new User(new UserId(userId));
        user.setUserName(new UserName("ユーザ" + userId));
        user.setPassword(new Password("password" + userId));
        user.setLoginId(new LoginId("test-password-" + userId));
        user.getAuthorities().add(Authority.ConfigChange);

        return user;
    }
}
