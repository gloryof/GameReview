package jp.ne.glory.infra.db.user.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.user.entity.User;
import jp.ne.glory.domain.user.repository.UserRepository;
import jp.ne.glory.domain.user.value.Authority;
import jp.ne.glory.domain.user.value.LoginId;
import jp.ne.glory.domain.user.value.Password;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import jp.ne.glory.domain.user.value.search.UserSearchCondition;
import jp.ne.glory.infra.db.user.dao.UserAccountDao;
import jp.ne.glory.infra.db.user.dao.UserAuthorityDao;
import jp.ne.glory.infra.db.user.dao.UserInfoDao;
import jp.ne.glory.infra.db.user.entity.UserAccount;
import jp.ne.glory.infra.db.user.entity.UserAuthority;
import jp.ne.glory.infra.db.user.entity.UserInfo;
import jp.ne.glory.infra.db.user.entity.UserListResult;
import jp.ne.glory.infra.db.user.entity.UserSearchParam;

/**
 * ユーザリポジトリ.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    /**
     * ユーザ情報DAO.
     */
    private final UserInfoDao userInfoDao;

    /**
     * ユーザアカウントDAO.
     */
    private final UserAccountDao userAccountDao;

    /**
     * ユーザ権限DAO.
     */
    private final UserAuthorityDao userAuthorityDao;

    /**
     * コンストラクタ.
     *
     * @param userInfoDao ユーザ情報DAO
     * @param userAccountDao ユーザアカウントDAO
     * @param userAuthorityDao ユーザ権限DAO
     */
    @Inject
    public UserRepositoryImpl(final UserInfoDao userInfoDao, final UserAccountDao userAccountDao,
            final UserAuthorityDao userAuthorityDao) {

        this.userInfoDao = userInfoDao;
        this.userAccountDao = userAccountDao;
        this.userAuthorityDao = userAuthorityDao;
    }

    /**
     * 全てのユーザを取得する.
     *
     * @return ユーザリスト
     */
    @Override
    public List<User> findAll() {

        return userInfoDao.getListResult().stream()
                .map(v -> convertToDomainEntity(v))
                .collect(Collectors.toList());
    }

    /**
     * ユーザを保存する.
     *
     * @param user ユーザ
     * @return 保存したユーザID
     */
    @Override
    public UserId save(final User user) {

        final long userId = saveUserInfo(user);
        saveUserAccount(user, userId);
        saveUserAuthority(user);

        return new UserId(userId);
    }

    /**
     * ユーザIDでユーザを探す.
     *
     * @param userId ユーザID
     * @return ユーザ
     */
    @Override
    public Optional<User> findBy(final UserId userId) {

        final Optional<UserInfo> optUserInfo = userInfoDao.selectById(userId.getValue());
        if (!optUserInfo.isPresent()) {

            return Optional.empty();
        }

        final Optional<UserAccount> optUserAccount = userAccountDao.selectById(userId.getValue());
        if (!optUserAccount.isPresent()) {

            return Optional.empty();
        }

        final List<UserAuthority> authorities = userAuthorityDao.selectByUserId(userId.getValue());

        final User user = convertToDomainEntity(optUserInfo.get(), optUserAccount.get(), authorities);
        return Optional.of(user);
    }

    /**
     * ログインIDでユーザを探す.
     *
     * @param loginId ログインID
     * @return ユーザ
     */
    @Override
    public Optional<User> findBy(final LoginId loginId) {

        final Optional<UserAccount> optUserAccount = userAccountDao.selectByLoginId(loginId.getValue());
        if (!optUserAccount.isPresent()) {

            return Optional.empty();
        }

        final Long userId = optUserAccount.get().getUserId();
        final Optional<UserInfo> optUserInfo = userInfoDao.selectById(userId);
        if (!optUserInfo.isPresent()) {

            return Optional.empty();
        }

        final List<UserAuthority> authorities = userAuthorityDao.selectByUserId(userId);

        final User user = convertToDomainEntity(optUserInfo.get(), optUserAccount.get(), authorities);
        return Optional.of(user);
    }

    /**
     * ユーザの検索を行う.
     *
     * @param condition 検索条件
     * @return ユーザリスト
     */
    @Override
    public List<User> search(final UserSearchCondition condition) {

        return userInfoDao.search(new UserSearchParam(condition)).stream()
                .map(v -> convertToDomainEntity(v))
                .collect(Collectors.toList());
    }

    /**
     * ユーザの検索結果件数を取得する.
     *
     * @param condition 検索条件
     * @return 検索結果件数
     */
    @Override
    public int getSearchCount(final UserSearchCondition condition) {
        return search(condition).size();
    }

    /**
     * ドメインのエンティティに変換する.
     *
     * @param userInfo ユーザ情報
     * @param userAccount ユーザ情報
     * @param authorities 権限リスト
     * @return ユーザ
     */
    private User convertToDomainEntity(final UserInfo userInfo, final UserAccount userAccount,
            List<UserAuthority> authorities) {

        final User user = new User(new UserId(userInfo.getUserId()));

        user.setUserName(new UserName(userInfo.getUserName()));
        user.setLoginId(new LoginId(userAccount.getLoginId()));
        user.setPassword(new Password(userAccount.getPassword()));

        authorities.forEach(v -> {
            final Authority authority = Authority.getByAuthorityId(v.getAuthorityId().intValue());
            user.getAuthorities().add(authority);
        });

        return user;
    }

    /**
     * ドメインのエンティティに変換する.
     *
     * @param result 一覧取得結果
     * @return ユーザ
     */
    private User convertToDomainEntity(final UserListResult result) {

        final User user = new User(new UserId(result.getUserId()));

        user.setUserName(new UserName(result.getUserName()));
        user.setLoginId(new LoginId(result.getLoginId()));
        user.setPassword(new Password(result.getPassword()));

        if (result.isConfChange()) {

            user.getAuthorities().add(Authority.ConfigChange);
        }

        if (result.isReviewPost()) {

            user.getAuthorities().add(Authority.ReviewPost);
        }

        return user;
    }

    /**
     * user_infoテーブルに保存する.
     *
     * @param user ユーザ
     * @return 登録後ユーザID
     */
    private long saveUserInfo(final User user) {

        if (user.isRegistered()) {

            return updateUserInfo(user);
        }

        return insertUserInfo(user);
    }

    /**
     * user_infoテーブルの内容を更新する.
     *
     * @param user ユーザ
     * @return ユーザID
     */
    private long updateUserInfo(final User user) {

        final UserId userId = user.getUserId();

        final Optional<UserInfo> optUserInfo = userInfoDao.selectById(userId.getValue());
        final UserInfo userInfo = optUserInfo.get();
        userInfo.setUserName(user.getUserName().getValue());
        userInfo.setLockUpdateTimestamp(LocalDateTime.now());

        userInfoDao.update(userInfo);

        return userId.getValue();
    }

    /**
     * user_infoテーブルの内容を登録する.
     *
     * @param user ユーザ
     * @return ユーザID
     */
    private long insertUserInfo(final User user) {

        final UserInfo userInfo = new UserInfo();

        final long userId = userInfoDao.getSequence();
        userInfo.setUserId(userId);
        userInfo.setUserName(user.getUserName().getValue());
        userInfo.setLockUpdateTimestamp(LocalDateTime.now());

        userInfoDao.insert(userInfo);

        return userId;
    }

    /**
     * user_accountテーブルに保存する.
     *
     * @param user ユーザ
     * @param targetUserId 対象ユーザID
     */
    private void saveUserAccount(final User user, final long targetUserId) {

        if (user.isRegistered()) {

            updteUserAccount(user, targetUserId);
            return;
        }

        insertUserAccount(user, targetUserId);
    }

    /**
     * user_accountテーブルの内容を更新する.
     *
     * @param user ユーザ
     * @param targetUserId 対象ユーザID
     */
    private void updteUserAccount(final User user, final long targetUserId) {

        final Optional<UserAccount> optUserAccount = userAccountDao.selectById(targetUserId);
        final UserAccount userAccount = optUserAccount.get();

        userAccount.setLoginId(user.getLoginId().getValue());
        userAccount.setPassword(user.getPassword().getValue());

        userAccountDao.update(userAccount);
    }

    /**
     * user_accountテーブルの内容を登録する.
     *
     * @param user ユーザ
     * @param targetUserId 対象ユーザID
     */
    private void insertUserAccount(final User user, final long targetUserId) {

        final UserAccount userAccount = new UserAccount();

        userAccount.setUserId(targetUserId);
        userAccount.setLoginId(user.getLoginId().getValue());
        userAccount.setPassword(user.getPassword().getValue());

        userAccountDao.insert(userAccount);
    }

    /**
     * user_authorityテーブルに権限情報を保存する.
     *
     * @param user ユーザ
     */
    private void saveUserAuthority(final User user) {

        userAuthorityDao.deleteByUserId(user.getUserId().getValue());

        final Function<Authority, UserAuthority> converter
                = v -> {
                    final UserAuthority authority = new UserAuthority();
            authority.setUserId(user.getUserId().getValue());
            authority.setAuthorityId(v.authorityId);

                    return authority;
                };

        final List<UserAuthority> insertTargets = Arrays.stream(Authority.values())
                .filter(v -> user.getAuthorities().hasAuthority(v))
                .map(converter)
                .collect(Collectors.toList());

        userAuthorityDao.batchInsert(insertTargets);
    }
}
