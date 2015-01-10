package jp.ne.glory.infra.db.repository.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;

@RequestScoped
public class UserRepositoryImpl implements UserRepository {

    private static final Map<Long, User> userMap = new HashMap<>();

    private static long sequence = 1;

    static {

        final User user = new User(new UserId(sequence++));

        user.loginId = new LoginId("test-user");
        user.password = new Password("password");
        user.userName = new UserName("テストユーザ");

        userMap.put(user.userId.value, user);
    }

    @Override
    public UserId save(final User user) {

        final User saveUser;
        if (user.userId == null) {

            saveUser = new User(new UserId(sequence));
            sequence++;

            Arrays.stream(Authority.values())
                    .filter(v -> user.authorities.hasAuthority(v))
                    .forEach(saveUser.authorities::add);
            saveUser.loginId = user.loginId;
            saveUser.userName = user.userName;

        } else {

            saveUser = user;
        }
        userMap.put(saveUser.userId.value, saveUser);

        return saveUser.userId;
    }

    @Override
    public Optional<User> findBy(final UserId userId) {

        return Optional.ofNullable(userMap.get(userId.value));
    }

    @Override
    public Optional<User> findBy(LoginId loginId) {
        return userMap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> v.loginId.value.equals(loginId.value))
                .findAny();
    }

}
