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
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;

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
        if (!user.isRegistered()) {

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

        final List<User> allList = searchAll(condition);

        if (condition.getLotPerCount() < 1) {

            return allList;
        }

        final int tempStartIndex = (condition.getLotNumber() - 1) * condition.getLotPerCount();

        final int startIndex = tempStartIndex < 1 ? 0 : tempStartIndex - 1;
        final int tempEndIndex = startIndex + condition.getLotPerCount();

        final int listLastIndex = allList.size() - 1;
        final int endIndex = listLastIndex < tempEndIndex ? listLastIndex : tempEndIndex;

        return allList.subList(startIndex, endIndex);
    }

    @Override
    public int getSearchCount(UserSearchCondition condition) {
        return searchAll(condition).size();
    }

    public long getCurrentSequence() {

        return sequence;
    }

    private boolean isMatchCondition(final UserSearchCondition condition, final User user) {

        final LoginId loginId = condition.getLoginId();
        final UserName userName = condition.getUserName();

        if (loginId != null && !loginId.getValue().isEmpty()) {

            if (!loginId.getValue().equals(user.getLoginId().getValue())) {

                return false;
            }
        }

        if (userName != null && !userName.getValue().isEmpty()) {

            if (!userName.getValue().equals(user.getUserName().getValue())) {

                return false;
            }
        }

        return true;
    }

    private List<User> searchAll(UserSearchCondition condition) {

        return userMap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> isMatchCondition(condition, v))
                .collect(Collectors.toList());
    }
}
