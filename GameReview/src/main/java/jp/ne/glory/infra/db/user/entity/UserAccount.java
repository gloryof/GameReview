package jp.ne.glory.infra.db.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ユーザアカウントテーブル.
 *
 * @author Junki Yamada
 */
@Entity
public class UserAccount {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    @Id
    private long userId;

    /**
     * ログインID.
     */
    @Getter
    @Setter
    private String loginId;

    /**
     * パスワード.
     */
    @Getter
    @Setter
    private String password;
}
