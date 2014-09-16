package jp.ne.glory.domain.user.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.UserId;

public class UserRepositoryStub implements UserRepository {

    private final Map<Long, User> userMap = new HashMap<>();

    private long sequence = 1;

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

}
