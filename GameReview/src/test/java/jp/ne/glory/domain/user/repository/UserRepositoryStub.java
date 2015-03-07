package jp.ne.glory.domain.user.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.UserId;

public class UserRepositoryStub implements UserRepository {

    private final Map<Long, User> userMap = new HashMap<>();

    private long sequence = 1;

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

}
