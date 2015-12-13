package jp.ne.glory.infra.db.user.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;

/**
 * 一覧で取得するための結果Bean.
 *
 * @author Junki Yamada
 */
@Entity
public class UserListResult {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    private long userId;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private String userName;

    /**
     * ロック管理タイムスタンプ.
     */
    @Getter
    @Setter
    private LocalDateTime lockUpdateTimestamp;

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

    /**
     * 権限変更の権限を持っているかのフラグ.
     */
    @Getter
    @Setter
    private boolean confChange;

    /**
     * 権限変更の権限を持っているかのフラグ.
     */
    @Getter
    @Setter
    private boolean reviewPost;

}
