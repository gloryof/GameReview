package jp.ne.glory.infra.db.repository.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;

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
    public List<User> findAll() {
        return userMap.entrySet().stream()
                .sorted((v1, v2) -> v1.getKey() < v2.getKey() ? -1 : 1)
                .map(v -> v.getValue()).collect(Collectors.toList());
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

    @Override
    public List<User> search(UserSearchCondition condition) {
        return userMap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> isMatchCondition(condition, v))
                .collect(Collectors.toList());
    }

    @Override
    public int getSearchCount(UserSearchCondition condition) {
        return search(condition).size();
    }

    private boolean isMatchCondition(final UserSearchCondition condition, final User user) {

        final LoginId loginId = condition.getLoginId();
        final UserName userName = condition.getUserName();

        if (loginId != null) {

            if (!loginId.getValue().equals(user.getLoginId().getValue())) {

                return false;
            }
        }

        if (userName != null) {

            if (!userName.getValue().equals(user.getUserName().getValue())) {

                return false;
            }
        }

        return true;
    }
}
