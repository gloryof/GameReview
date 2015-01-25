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

        user.setLoginId(new LoginId("test-user"));
        user.setPassword(new Password("password"));
        user.setUserName(new UserName("テストユーザ"));

        userMap.put(user.getUserId().getValue(), user);
    }

    @Override
    public UserId save(final User user) {

        final User saveUser;
        if (user.getUserId() == null) {

            saveUser = new User(new UserId(sequence));
            sequence++;

            Arrays.stream(Authority.values())
                    .filter(v -> user.getAuthorities().hasAuthority(v))
                    .forEach(saveUser.getAuthorities()::add);
            saveUser.setLoginId(user.getLoginId());
            saveUser.setUserName(user.getUserName());

        } else {

            saveUser = user;
        }
        userMap.put(saveUser.getUserId().getValue(), saveUser);

        return saveUser.getUserId();
    }

    @Override
    public Optional<User> findBy(final UserId userId) {

        return Optional.ofNullable(userMap.get(userId.getValue()));
    }

    @Override
    public Optional<User> findBy(LoginId loginId) {
        return userMap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> v.getLoginId().getValue().equals(loginId.getValue()))
                .findAny();
    }

}
